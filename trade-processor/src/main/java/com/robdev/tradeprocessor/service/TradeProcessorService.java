package com.robdev.tradeprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@RequiredArgsConstructor
@Component
public class TradeProcessorService implements ApplicationListener<ApplicationReadyEvent> {
    private final KafkaReceiver<Object, Object> kafkaReceiver;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        kafkaReceiver.receive()
                .subscribe(d -> {
                    log.info("Record Key: " + d.key());
                    log.info("Record Value: " + d.value());
                    log.info("At Offset: " + d.offset());
                });
    }
}