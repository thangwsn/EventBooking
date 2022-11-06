package com.eticket.infrastructure.kafka.producer;

import com.eticket.infrastructure.utils.Utils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
public class KafkaSendService {
    private final Logger log = LoggerFactory.getLogger(KafkaSendService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaSender kafkaSender;

    public <T> void send(String topic, T object) {
        kafkaTemplate.send(topic, Utils.convertObjectToJson(object));
    }

    public <T> void reactorSend(String topic, T object, String uuid) {
        String json = Utils.convertObjectToJson(object);
        kafkaSender.<String>send(Flux.just(json)
                        .map(i -> SenderRecord.create(new ProducerRecord<>(topic, uuid, i), uuid)))
                .doOnError(e -> log.error("Send failed", e))
                .subscribe();
    }
}
