package com.example.project1;

import com.example.project1.entity.AbstractEntity;
import com.example.project1.entity.Order;
import com.example.project1.entity.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaListeners {

	@KafkaListener(topics = "${spring.kafka.producer.topicName}", // project isimli topici dinliyoruz
		groupId = "groupId" // dinleyicinin hangi grup kimliğine ait olduğunu belirledik
	) // kafka topic i dinler.

	void listener(List<ConsumerRecord<String, AbstractEntity>> consumerRecords){
		consumerRecords.stream().map(ConsumerRecord::value).forEach(value -> {
			if (value instanceof Order value1) {
				System.out.println("Listener received: " + value1 + "~");
			} else {
				User value2 = (User) value;
				System.out.println("Listener received: " + value2 + "~");
			}

		});

	} // kafkadan topicten gelen mesajları yazdırdık.
}
