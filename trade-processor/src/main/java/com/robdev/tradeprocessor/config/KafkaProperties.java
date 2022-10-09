package com.robdev.tradeprocessor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka")
public record KafkaProperties(String bootstrapServers,
    String consumerGroup,
    String inboundTopic,
    String outboundTopic,
    String keySerializer,
    String valueSerializer,
    String keyDeserializer,
    String valueDeserializer) {

}

