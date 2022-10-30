package com.example.demoprocessor.r4j.bindings;

import com.example.demoprocessor.factory.BindingsFactory;
import org.springframework.stereotype.Service;


@Service
public class StyleConsumerBindings extends ConsumerBindingsImpl {
    static String STYLE_BINDING="processStyle-in-0";
    protected StyleConsumerBindings(BindingsFactory bindingsFactory) {
        super(STYLE_BINDING, bindingsFactory);
    }
}
