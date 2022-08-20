package com.robdev.tradeprocessor.service;

import com.robdev.tradeprocessor.config.KafkaProperties;
import com.robdev.tradeprocessor.enrichment.Trade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

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
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        Flux<ReceiverRecord<String, String>> kafkaFlux = kafkaReceiver.receive();

        kafkaFlux.doOnNext(record -> {
            log.info(LOG_INCOMING_MESSAGE_FORMAT, record.topic(), record.partition(), record.offset(), record.key(), record.value());

            var tradeFlux = Mono.just(record)
                    .map(mapToTrade())
                    .map(composedTradeEnrichmentFunctions)
                    .flatMapMany(Flux::fromIterable);

            kafkaSender.send(tradeFlux.map(mapToJson())
                            .map(json -> SenderRecord.create(new ProducerRecord<>(kafkaProperties.getOutboundTopic(), json), 1)))
                    .doOnNext(result -> log.info("Result of message send {}", result.recordMetadata().timestamp()))
                    .subscribe();
            record.receiverOffset().acknowledge();
        }).subscribe();
    }
}