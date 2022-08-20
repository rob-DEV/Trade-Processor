package com.robdev.tradeprocessor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

public class TradeProcessorConfiguration {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
