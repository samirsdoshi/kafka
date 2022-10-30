package com.example.demoprocessor.factory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerFactoryImpl implements CircuitBreakerFactory {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerFactoryImpl(CircuitBreakerRegistry circuitBreakerRegistry){
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public CircuitBreaker getCircuitBreaker(String cbInstanceName) {
        return circuitBreakerRegistry.circuitBreaker(cbInstanceName);
    }
}
