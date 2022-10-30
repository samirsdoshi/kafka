package com.example.demoprocessor.r4j.bindings;

import com.example.demoprocessor.r4j.exception.UnknownStateException;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;

public interface CBBindingManager {

    void changeBindingState(CircuitBreakerOnStateTransitionEvent event,
                            ConsumerBindings consumerBindings) throws UnknownStateException, UnknownStateException;
}
