package com.robdev.tradeprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    public KafkaReceiver<String, String> kafkaReceiver(KafkaProperties kafkaProperties) {
        final Map<String, Object> consumerProps = Map.of(
                KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer(),
                VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer(),
                GROUP_ID_CONFIG, kafkaProperties.getConsumerGroup(),
                BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()
        );

        ReceiverOptions<String, String> consumerOptions = ReceiverOptions.<String, String>create(consumerProps)
                .subscription(List.of(kafkaProperties.getInboundTopic()))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        return KafkaReceiver.create(consumerOptions);
    }

    @Bean
    public KafkaSender<String, String> kafkaSender(KafkaProperties kafkaProperties) {
        final Map<String, Object> producerProps = Map.of(
                KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer(),
                VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer(),
                BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()
        );

        SenderOptions<String, String> senderOptions = SenderOptions.create(producerProps);
        return KafkaSender.create(senderOptions);
    }

}
