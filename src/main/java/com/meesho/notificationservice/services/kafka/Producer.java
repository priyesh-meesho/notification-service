package com.meesho.notificationservice.services.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.meesho.notificationservice.constants.Constants.KAFKA_TOPIC_NAME;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Long messageId) {
        logger.info(String.format("#### -> Producing message -> %s", messageId));
        this.kafkaTemplate.send(KAFKA_TOPIC_NAME, String.valueOf(messageId));
    }
}
