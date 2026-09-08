package com.ticketbooking.config;

import com.ticketbooking.event.BookingCreatedEvent;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    // KAFKA SERVER
    private final String bootstrapServers = "localhost:9092";
    // PRODUCER
    @Bean
    public ProducerFactory<String, BookingCreatedEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        // Kafka server
        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );
        // Message key serializer
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        // Message value serializer
        // BookingCreatedEvent -> JSON
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JacksonJsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }


    // =========================================================
    // CONSUMER
    // =========================================================

    @Bean
    public ConsumerFactory<String, BookingCreatedEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        // Kafka server
        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        // IMPORTANT:
        // Do NOT set GROUP_ID_CONFIG here.
        //
        // Each @KafkaListener will have its own group:
        //
        // email-group
        // notification-group
        // analytics-group
        //

        // Message key deserializer
        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        // Message value deserializer
        // JSON -> BookingCreatedEvent
        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JacksonJsonDeserializer.class
        );

        // Allow Jackson to deserialize our event class
        config.put(
                "spring.json.trusted.packages",
                "com.ticketbooking"
        );

        // Tell Kafka which Java class should be created
        // from the incoming JSON
        config.put(
                "spring.json.value.default.type",
                "com.ticketbooking.event.BookingCreatedEvent"
        );

        // If no previous offset exists,
        // start reading from the beginning.
        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        return new DefaultKafkaConsumerFactory<>(config);
    }


    // =========================================================
    // KAFKA LISTENER CONTAINER
    // =========================================================

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingCreatedEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BookingCreatedEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}