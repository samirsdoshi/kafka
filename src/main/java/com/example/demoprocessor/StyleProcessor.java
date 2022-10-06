package com.example.demoprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
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
    public Consumer<Message<StyleDTO>> processStyle() {
        return message-> {
            Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT,
                    Acknowledgment.class);
            System.out.println("ProcessStyle1 got:" +  message.getPayload().toJSON() + ", headers:" + message.getHeaders());
            acknowledgment.acknowledge();
        };
    }

    @Bean
    public Consumer<Message<List<?>>> processStyleBatch(){
        return message ->{
            System.out.println("Headers:"  + message.getHeaders());
            message.getPayload().forEach(style->{
                StyleDTO styleDTO= StyleDTO.fromJSON(style.toString());
                System.out.println("got:" + styleDTO.toJSON());
            });
            message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class).acknowledge();
        };
    }

}