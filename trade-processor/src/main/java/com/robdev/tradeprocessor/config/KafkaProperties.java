package com.robdev.tradeprocessor.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaProperties {

    private String bootstrapServers;
    private String consumerGroup;
    private String inboundTopic;
    private String outboundTopic;
    private String keySerializer;
    private String valueSerializer;
    private String keyDeserializer;
    private String valueDeserializer;

}

