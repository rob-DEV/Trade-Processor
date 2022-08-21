package com.robdev.tradeprocessor.service;

import com.robdev.tradeprocessor.config.KafkaProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import static com.robdev.tradeprocessor.enrichment.TradeEnrichmentFunctions.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TradeProcessor implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaProperties kafkaProperties;
    private final KafkaReceiver<String, String> kafkaReceiver;
    private final KafkaSender<String, String> kafkaSender;

    private final String LOG_INCOMING_MESSAGE_FORMAT = "Received message: topic-partition={}-{} offset={} key={} value={}";

    @Override
    public void onApplicationEvent(@NonNull final ApplicationReadyEvent event) {
        Flux<ConsumerRecord<String, String>> kafkaFlux = kafkaReceiver.receiveAtmostOnce();

        kafkaFlux.map(record -> {
                    log.info(LOG_INCOMING_MESSAGE_FORMAT, record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    return Mono.just(record)
                            .map(mapToTrade())
                            .map(composedTradeEnrichmentFunctions)
                            .flatMapMany(Flux::fromIterable);
                })
                .doOnError(err -> log.error("Enrichment Error: {}", err.getMessage()))
                .subscribe(s -> {
                    Flux<SenderResult<Integer>> senderResultFlux = kafkaSender.send(s
                            .map(mapToJson())
                            .map(json -> SenderRecord.create(new ProducerRecord<>(kafkaProperties.getOutboundTopic(), json), 1))
                    );
                    senderResultFlux.doOnError(err -> log.info("Publishing Error {}", err.getMessage()));
                    senderResultFlux.subscribe(r -> {
                        log.info("Sent message at: {}", r.recordMetadata().timestamp());
                    });
                });
    }
}