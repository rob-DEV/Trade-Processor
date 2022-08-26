package com.robdev.tradeprocessor.service;

import com.robdev.tradeprocessor.config.KafkaProperties;
import com.robdev.tradeprocessor.enrichment.Trade;
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

import java.util.concurrent.atomic.AtomicInteger;

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

        Flux<Trade> enrichmentFlux = kafkaFlux
                .map(mapToTrade())
                .map(composedTradeEnrichmentFunctions)
                .concatMapIterable(s -> s.subList(0,s.size()-1))
                .log()
                .doOnError(err -> log.error("Enrichment Error: {}", err.getMessage()));

        Flux<SenderResult<Integer>> senderFlux =  kafkaSender.send(enrichmentFlux
                .map(mapToJson())
                .map(json -> SenderRecord.create(new ProducerRecord<>(kafkaProperties.getOutboundTopic(), json), 1)))
                .doOnError(err -> log.info("Publishing Error {}", err.getMessage()));

        enrichmentFlux.subscribe();
        senderFlux.subscribe();
    }
}