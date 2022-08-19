package com.robdev.tradeprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    public KafkaReceiver<Object, Object> kafkaReceiver(KafkaProperties kafkaProperties) {
        final Map<String, Object> consumerProps = Map.of(
                KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer(),
                VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer(),
                GROUP_ID_CONFIG, kafkaProperties.getConsumerGroup(),
                BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()
        );

        ReceiverOptions<Object, Object> consumerOptions = ReceiverOptions.create(consumerProps)
                .subscription(List.of(kafkaProperties.getInboundTopic()))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        return KafkaReceiver.create(consumerOptions);
    }

}
