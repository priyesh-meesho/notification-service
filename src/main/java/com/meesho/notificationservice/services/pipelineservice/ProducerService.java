package com.meesho.notificationservice.services.pipelineservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.meesho.notificationservice.constants.Constants.KAFKA_TOPIC_NAME;
import static com.meesho.notificationservice.constants.Constants.LOGGER_NAME;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Long messageId) {
        logger.info(String.format("sending message id from producer side to pipeline -> %s", messageId));
        this.kafkaTemplate.send(KAFKA_TOPIC_NAME, String.valueOf(messageId));
    }
}