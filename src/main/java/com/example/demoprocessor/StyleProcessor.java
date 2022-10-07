package com.example.demoprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.config.SpringIntegrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;


@Component
public class StyleProcessor {

    private static final Log logger = LogFactory.getLog(StyleProcessor.class);

    @Bean
    public Consumer<Message<?>> processStyle() {
        return message-> {
            Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT,
                    Acknowledgment.class);
            System.out.println("ProcessStyle got:" +  message.getPayload().toString() + ", headers:" + message.getHeaders());
            acknowledgment.acknowledge();
        };
    }

    @Bean
    public Consumer<KStream<String, ?>> processStyleKStream() {
        return message-> {
            message.peek((k,v)->{
                StyleDTO styleDTO = StyleDTO.fromJSON(v.toString());
                System.out.println("ProcessStyleKStream Got:" + styleDTO.toJSON());
            });
        };
    }

    @KafkaListener(id = "kListener", topics = "TP.STYLE")
    public void listen(@Payload(required = false) com.example.demoprocessor.Style v, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        StyleDTO styleDTO = StyleDTO.fromJSON(v.toString());
        System.out.println("kafkaListener Got:" + styleDTO.toJSON() + ", key:" + key);
    }

    @Bean
    public Consumer<Message<List<?>>> processStyleBatch(){
        return message ->{
            try {
                System.out.println("Headers:" + message.getHeaders());
                IntStream.range(0,message.getPayload().size()).forEach(idx ->{;
                    StyleDTO styleDTO = StyleDTO.fromJSON(message.getPayload().get(idx).toString());
                    System.out.println("got:" + styleDTO.toJSON());
                    if (idx==5){
                        throw new RuntimeException("Testing");
                    }
                });
                message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class).acknowledge();
            }catch(Exception e){
               System.out.println(e.getMessage());
               throw e;
            }
        };
    }

}