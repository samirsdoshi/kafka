package com.example.demoprocessor.r4j.bindings;


import com.example.demoprocessor.factory.BindingsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.Binding;

public abstract class ConsumerBindingsImpl implements ConsumerBindings {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerBindingsImpl.class);
    private final String bindingName;
    private final BindingsFactory bindingsFactory;

    protected ConsumerBindingsImpl(String bindingName, BindingsFactory bindingsFactory) {
        this.bindingName = bindingName;
        this.bindingsFactory = bindingsFactory;
    }

    @Override
    public void pauseBinding() {
        final Binding<?> binding = bindingsFactory.getBinding(bindingName);
        binding.pause();
        LOGGER.info("Paused binding: {}. Is paused: {}", binding.getBindingName(), binding.isPaused());
    }

    @Override
    public void resumeBinding() {
        final Binding<?> binding = bindingsFactory.getBinding(bindingName);
        binding.resume();
        LOGGER.info("Resumed binding: {}. Is paused: {}", binding.getBindingName(), binding.isPaused());
    }
}
