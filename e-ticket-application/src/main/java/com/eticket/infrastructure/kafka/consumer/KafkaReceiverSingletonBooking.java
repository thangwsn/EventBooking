package com.eticket.infrastructure.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class KafkaReceiverSingletonBooking {
    private static KafkaReceiverSingletonBooking single_instance = null;
    private final Logger log = LoggerFactory.getLogger(KafkaReceiverSingletonBooking.class);
    private Flux<ConsumerRecord<String, String>> kafkaFlux;

    private KafkaReceiverSingletonBooking(ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate) {
        kafkaFlux = kafkaConsumerTemplate
                .receiveAutoAck()
                .subscribeWith(EmitterProcessor.create(false))
                .subscribeOn(Schedulers.newSingle("kafka-consumer"));
        ;
    }

    public static KafkaReceiverSingletonBooking getInstance(ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate) {
        if (single_instance == null)
            single_instance = new KafkaReceiverSingletonBooking(kafkaConsumerTemplate);
        return single_instance;
    }

    public static KafkaReceiverSingletonBooking getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(KafkaReceiverSingletonBooking single_instance) {
        KafkaReceiverSingletonBooking.single_instance = single_instance;
    }

    public Flux<ConsumerRecord<String, String>> getKafkaFlux() {
        return kafkaFlux;
    }

    public void setKafkaFlux(Flux<ConsumerRecord<String, String>> kafkaFlux) {
        this.kafkaFlux = kafkaFlux;
    }

}
