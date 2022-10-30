package com.example.demoprocessor.r4j.bindings;

import com.example.demoprocessor.r4j.exception.UnknownStateException;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CBBindingManagerImpl implements CBBindingManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CBBindingManagerImpl.class);
    @Override
    public void changeBindingState(CircuitBreakerOnStateTransitionEvent event,
                                   ConsumerBindings consumerBindings) throws UnknownStateException {
        LOGGER.info("circuit breaker event: {}", event);
        switch (event.getStateTransition()) {
            case CLOSED_TO_OPEN:
            case CLOSED_TO_FORCED_OPEN:
            case HALF_OPEN_TO_OPEN:
                consumerBindings.pauseBinding();
                break;

            case OPEN_TO_HALF_OPEN:
            case HALF_OPEN_TO_CLOSED:
            case FORCED_OPEN_TO_CLOSED:
            case FORCED_OPEN_TO_HALF_OPEN:
                consumerBindings.resumeBinding();
                break;

            default:
                throw new UnknownStateException("Unknown transition state: " + event.getStateTransition());
        }
    }
}
