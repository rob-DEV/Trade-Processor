package com.robdev.tradeprocessor.enrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robdev.tradeprocessor.util.CommonUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TradeEnrichmentFunctions {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Function<ReceiverRecord<String, String>, Trade> mapToTrade() {
        return data -> {
            try {
                return objectMapper.readValue(data.value(), Trade.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
//        return data -> CommonUtil.resultOrExcept((Supplier<Trade>) () -> objectMapper.readValue(data.value(),));
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
        return trade -> trade;
    }

    public static Function<Trade, List<Trade>> splitTradesToFlux() {
        return data -> List.of(data, data);
    }

    public static Function<Trade, List<Trade>> composedTradeEnrichmentFunctions =
            enrichTradingInstrumentFromUnderlyingSource()
                    .andThen(splitTradesToFlux());
}
