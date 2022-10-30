package com.example.demoprocessor.r4j.config;

import com.example.demoprocessor.factory.CircuitBreakerFactory;
import com.example.demoprocessor.r4j.bindings.CBBindingManager;
import com.example.demoprocessor.r4j.bindings.StyleConsumerBindings;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service(value="StyleBindingCBConfigurer")
public class StyleBindingCBConfigurer extends CBConfigurer {

    static String STYLE_CONSUMER_CIRCUIT_BREAKER="styleCB";
    io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;
    public StyleBindingCBConfigurer(StyleConsumerBindings styleConsumerBindings,
                                    CircuitBreakerFactory circuitBreakerFactory,
                                    CBBindingManager cbBindingManager) {
        super(styleConsumerBindings, STYLE_CONSUMER_CIRCUIT_BREAKER, circuitBreakerFactory, cbBindingManager);
        this.circuitBreaker= circuitBreakerFactory.getCircuitBreaker(STYLE_CONSUMER_CIRCUIT_BREAKER);
    }
}
