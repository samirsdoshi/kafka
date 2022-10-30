package com.example.demoprocessor.factory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public interface CircuitBreakerFactory {

    CircuitBreaker getCircuitBreaker(String cbInstanceName);
}
