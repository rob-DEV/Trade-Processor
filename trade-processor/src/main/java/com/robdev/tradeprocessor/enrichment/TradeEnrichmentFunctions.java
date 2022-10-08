package com.robdev.tradeprocessor.enrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.function.Function;

@Slf4j
@FieldDefaults(makeFinal = true)
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

    public static Function<Trade, Trade> enrichTradeId() {
        return data -> {
            data.setTradeID(12345678);
            return data;
        };
    }

    public static Function<Trade, List<Trade>> copyTrade() {
        return data -> List.of(data, data);
    }

    public static Function<ConsumerRecord<String, String>, List<Trade>> COMPOSED_TRADE_ENRICHMENT_FUNCTIONS =
            mapToTrade()
                    .andThen(enrichTradeId())
                    .andThen(copyTrade());
}
