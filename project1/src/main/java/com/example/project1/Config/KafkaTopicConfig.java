package com.example.project1.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@ConditionalOnProperty("${kafka.enabled}")
@EnableKafka
@Configuration
public class KafkaTopicConfig {
	@Bean
	public NewTopic projectTopic(){
		return TopicBuilder.name("project").build();
		//kafkada yeni bir topic oluşturduk ve adı project oldu.

	}
}
