package com.example.demoprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.function.Consumer;


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

    @Bean
    public Consumer<Message<List<?>>> processStyleBatch(){
        return message ->{
            try {
                System.out.println("Headers:" + message.getHeaders());
                message.getPayload().forEach(style -> {
                    StyleDTO styleDTO = StyleDTO.fromJSON(style.toString());
                    System.out.println("got:" + styleDTO.toJSON());
                });
                message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class).acknowledge();
            }catch(Exception e){

            }
        };
    }

}