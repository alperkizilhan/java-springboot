package com.example.project1.service;

import com.example.project1.entity.AbstractEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

	@Value("${spring.kafka.producer.topicName}")
	private String topicName;

	public KafkaService(KafkaTemplate<String, AbstractEntity> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	private KafkaTemplate<String, AbstractEntity> kafkaTemplate;

	public void sendData(AbstractEntity abstractEntity){
		kafkaTemplate.send(topicName, abstractEntity); // Kafka mesajı ürettik
	}

}
