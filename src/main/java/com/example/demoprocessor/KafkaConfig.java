package com.example.demoprocessor;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value(value = "localhost:9092")
    private String bootstrapAddress;

    @Value(value = "kListener")
    private String groupId;


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", "true");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,10);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    ConsumerRecordRecoverer recoverer=new ConsumerAwareRecordRecoverer() {
        @Override
        public void accept(ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, Exception exception) {
            System.out.println("In Record Recoverer." + exception.getMessage());
        }
    };


    @Bean(name="eh")
    KafkaListenerErrorHandler eh() {
        return (msg, ex) -> {
            if (msg.getHeaders().get(KafkaHeaders.DELIVERY_ATTEMPT, Integer.class) > 2) {
                recoverer.accept(msg.getHeaders().get(KafkaHeaders.RAW_DATA, ConsumerRecord.class), ex);
                return "FAILED";
            }
            throw ex;
        };
    }

    BackOff backOff=new FixedBackOff(0L, 2L);
   // DefaultErrorHandler errorHandler = new StyleBatchErrorHandler(recoverer, backOff);
    DefaultErrorHandler errorHandler= new DefaultErrorHandler((record, exception) -> {
       // recover after 3 failures, with no back off - e.g. send to a dead-letter topic
       System.out.println("Recovering:" + record.value().toString());
   }, backOff);


    @Bean
    public ListenerContainerCustomizer<AbstractMessageListenerContainer<String, Object>> customizer() {
        return (container, destination, group) -> container.setCommonErrorHandler(errorHandler);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer streamsCustomizer() {
        return new StreamsBuilderFactoryBeanConfigurer() {
            @Override
            public void configure(StreamsBuilderFactoryBean factoryBean) {
                factoryBean.setStreamsUncaughtExceptionHandler(getStreamsUncaughtExceptionHandler());
            }
            @Override
            public int getOrder() {
                return Integer.MAX_VALUE - 10000;
            }
        };
    }

    private StreamsUncaughtExceptionHandler getStreamsUncaughtExceptionHandler() {
        return new MaxFailuresUncaughtExceptionHandler(3, 50);
    }

}
