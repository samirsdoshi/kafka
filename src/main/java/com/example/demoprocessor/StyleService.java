package com.example.demoprocessor;

import com.example.demoprocessor.exceptions.DemoRetryableException;
import com.example.demoprocessor.r4j.config.CBConfigurer;
import com.example.demoprocessor.r4j.config.StyleBindingCBConfigurer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StyleService {
    @Resource
    StyleBindingCBConfigurer cbConfigurer;

    @CircuitBreaker(name="styleCB")
    @Retry(name="demoretry")
    public void processMessageCB(Message<com.example.test.Style> message) {
        //simulate retry. fail for 2 times and succeed on 3rd attempt
        if (message.getPayload().getDescription().equals("Desc_1")) {
            System.out.println("In processMessageCB");
            //throw new NullPointerException("Non Retryable exception");
        }else if (message.getPayload().getDescription().equals("Desc_2")) {
            //System.out.println("In processMessageCB - force open");
            //cbConfigurer.getCircuitBreaker().transitionToForcedOpenState();
            throw new DemoRetryableException("Retryable Exception");
        }else{
            System.out.println("In processMessageCB: Processed " +  message.getPayload().getDescription());
        }
    }
}
