package com.example.demoprocessor.factory;

import org.springframework.cloud.stream.binder.Binding;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.stereotype.Service;

@Service
public class BindingsFactoryImpl implements BindingsFactory {

    private final BindingsLifecycleController bindingsLifecycleController;

    public BindingsFactoryImpl(BindingsLifecycleController bindingsLifecycleController){
        this.bindingsLifecycleController = bindingsLifecycleController;
    }

    @Override
    public Binding<?> getBinding(String bindingName) {
        return bindingsLifecycleController.queryState(bindingName);
    }
}
