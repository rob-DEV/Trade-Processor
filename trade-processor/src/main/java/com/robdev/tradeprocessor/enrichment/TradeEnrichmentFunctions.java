package com.robdev.tradeprocessor.enrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class TradeEnrichmentFunctions {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Function<ConsumerRecord<String, String>, Trade> mapToTrade() {
        return data -> {
            try {
                return objectMapper.readValue(data.value(), Trade.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Function<Trade, String> mapToJson() {
        return data -> {
            try {
                return objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Function<Trade, Trade> enrichTradingInstrumentFromUnderlyingSource() {
        return trade -> {
            log.info("THREAD ID {}", Thread.currentThread().getId());
            trade.setTradeID((int)Thread.currentThread().getId());
            return trade;
        };
    }

    public static Function<Trade, List<Trade>> splitTradesToFlux() {
        // Copy the message
        return data -> List.of(data, data);
    }

    public static Function<Trade, List<Trade>> composedTradeEnrichmentFunctions =
            enrichTradingInstrumentFromUnderlyingSource()
                    .andThen(splitTradesToFlux());
}
