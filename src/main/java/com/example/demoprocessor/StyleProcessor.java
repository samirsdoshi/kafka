package com.example.demoprocessor;

import com.example.demoprocessor.exceptions.DemoRetryableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.springframework.kafka.support.KafkaHeaders.DELIVERY_ATTEMPT;


@Component
@Service
public class StyleProcessor {

    private static final Log logger = LogFactory.getLog(StyleProcessor.class);

    @Bean
    public Consumer<Message<?>> processStyle() {
        return message-> {
            Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT,
                    Acknowledgment.class);
            if (true) {
                //simulate retry. fail for 2 times and succeed on 3rd attempt
                AtomicInteger retry=(AtomicInteger)message.getHeaders().get("deliveryAttempt");
                if (retry!=null && retry.get() < 3){
                    System.out.println("Throwing exception retry count " + retry.get());
                    throw new DemoRetryableException("test");
                }
            }
            System.out.println("ProcessStyle got:" +  message.getPayload().toString() + ", headers:" + message.getHeaders());
            acknowledgment.acknowledge();
        };
    }

    @Bean
    public Consumer<KStream<String, com.example.test.Style>> processStyleKStream() {
        return message-> {
            message.peek((k,v)->{
                StyleDTO styleDTO = new StyleDTO(v);
                System.out.println("ProcessStyleKStream Got:" + styleDTO.toJSON());
            });
        };
    }

    @KafkaListener(id = "kListener", topics = "TP.STYLE")
    public void listen(@Payload(required = false) com.example.test.Style v, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        StyleDTO styleDTO = new StyleDTO(v);
        System.out.println("kafkaListener Got:" + styleDTO.toJSON() + ", key:" + key);
    }

    @Bean
    public Consumer<Message<List<com.example.test.Style>>> processStyleBatch(){
        return message ->{
            //System.out.println("Headers:" + message.getHeaders());
            IntStream.range(0, message.getPayload().size()).forEach(idx -> {
                ;
                StyleDTO styleDTO = new StyleDTO(message.getPayload().get(idx));
                System.out.println("got:index:" + idx + ":" + styleDTO.toJSON());
                if (styleDTO.getDescription().equals("Desc_4")) {
                    throw new BatchListenerFailedException("Failed to process", idx);
                }
                if (styleDTO.getDescription().equals("Desc_5")) {
                    throw new DemoRetryableException("Retry5");
                }

            });
            message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class).acknowledge();
        };
    }


//    @KafkaListener(topics="TP.STYLE.BATCH",containerFactory="batchListenerContainerFactory",batch="true", errorHandler = "eh")
//    public void processStyleBatch(@Payload List< com.example.demoprocessor.Style> styles, Acknowledgment acknowledgment){
//            try {
//                //System.out.println("Headers:" + message.getHeaders());
//                IntStream.range(0,styles.size()).forEach(idx ->{;
//                    StyleDTO styleDTO = StyleDTO.fromJSON(styles.get(idx).toString());
//                    System.out.println("got:index:" + idx + ":" + styleDTO.toJSON());
//                    if (styleDTO.getDescription().equals("Desc_4")){
//                        throw new BatchListenerFailedException("Failed to process", idx);
//                    }
//                });
//                acknowledgment.acknowledge();
//            }catch(Exception e){
//                //System.out.println(e.getMessage());
//                throw e;
//            }
//        };
}