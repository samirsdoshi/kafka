package com.example.demoprocessor.factory;

import org.springframework.cloud.stream.binder.Binding;

public interface BindingsFactory {

    Binding<?> getBinding(String bindingName);
}
