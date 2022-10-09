package com.robdev.tradeprocessor.service;

import com.robdev.tradeprocessor.config.KafkaProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TradeProcessor {

    KafkaProperties kafkaProperties;
    KafkaReceiver<String, String> kafkaReceiver;
    KafkaSender<String, String> kafkaSender;
    Disposable.Composite disposables = Disposables.composite();

    @PostConstruct
    public void connect() {
        disposables.add(
                receive()
                        .doOnError(error -> log.info( "Error in Kafka Receiver flow", error))
                        .subscribe(s -> log.info("Ended subscription to Kafka Receiver"))
        );
    }

    @PreDestroy
    public void disconnect() {
        this.disposables.dispose();
    }

    public Flux<SenderResult<UUID>> receive() {
        return kafkaReceiver.receive()
                .doOnNext(record -> log.info("Received record with key={}", record.key()))
                .flatMap(this::process);
    }

    public Flux<SenderResult<UUID>> process(ReceiverRecord<String, String> record) {
        var senderRecord = SenderRecord.create(
                new ProducerRecord<>(
                        kafkaProperties.getOutboundTopic(),
                        null,
                        record.key(),
                        record.value(),
                        null
                ),
                UUID.randomUUID()
        );

        log.info("Sending message with key={}", senderRecord.key());
        return kafkaSender.send(Mono.just(senderRecord));
    }
}