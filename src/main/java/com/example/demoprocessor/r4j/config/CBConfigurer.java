package com.example.demoprocessor.r4j.config;

import com.example.demoprocessor.factory.CircuitBreakerFactory;
import com.example.demoprocessor.r4j.bindings.CBBindingManager;
import com.example.demoprocessor.r4j.bindings.CBBindingManagerImpl;
import com.example.demoprocessor.r4j.bindings.ConsumerBindings;
import com.example.demoprocessor.r4j.exception.UnknownStateException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class CBConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CBBindingManagerImpl.class);
    private final ConsumerBindings consumerBindings;
    private final CircuitBreaker circuitBreaker;
    private final CBBindingManager cbBindingManager;

    protected CBConfigurer(ConsumerBindings consumerBindings,
                           String cbName,
                           CircuitBreakerFactory circuitBreakerFactory,
                           CBBindingManager bindingManager) {
        this.consumerBindings = consumerBindings;
        this.circuitBreaker = circuitBreakerFactory.getCircuitBreaker(cbName);
        this.cbBindingManager = bindingManager;
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    @PostConstruct
    public void registerEventConsumer() {
        LOGGER.debug("registerEventConsumer");
        circuitBreaker.getEventPublisher().onStateTransition(this::consumeEvent);
    }

    private void consumeEvent(CircuitBreakerOnStateTransitionEvent event) {
        try {
            LOGGER.debug("consumeEvent");
            cbBindingManager.changeBindingState(event, consumerBindings);
        } catch (UnknownStateException e) {
            LOGGER.error("Unknown state transition occurred", e);
        }
    }
}
