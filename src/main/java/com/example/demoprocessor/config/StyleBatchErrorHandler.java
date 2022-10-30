package com.example.demoprocessor.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.backoff.BackOff;

import java.util.List;

public class StyleBatchErrorHandler extends DefaultErrorHandler {
    public StyleBatchErrorHandler(ConsumerRecordRecoverer recoverer, BackOff backOff) {
        super(recoverer, backOff);
    }

    @Override
    public void handleRecord(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        super.handleRecord(thrownException, record, consumer, container);
    }

    @Override
    public void handleBatch(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container, Runnable invokeListener) {
            super.handleBatch(thrownException, data, consumer, container, invokeListener);
    }

    @Override
    public void handleRemaining(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        super.handleRemaining(thrownException, records, consumer, container);
    }
}
