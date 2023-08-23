package com.example.project1.Config;

import com.example.project1.entity.AbstractEntity;
import com.example.project1.entity.AbstractEntitySerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty("${kafka.enabled}")
@EnableKafka
@Configuration
public class KafkaProducerConfig {
	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String boostrapServers;

	public Map<String, Object> producerConfig(){
		HashMap<String,Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, AbstractEntitySerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AbstractEntitySerializer.class);
		return props;
	} // Kafka üretici yapılandırma ayarları içeren bir hasmap oluşturduk.

	@Bean
	public ProducerFactory<String, AbstractEntity> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerConfig());
	} // Kafka üretici fabrikasını oluşturduk.
	// Üretici fabrikası üretici yapılandırmalarını kullanarak Kafka üretici nesnelerini oluşturur.
	@Bean
	public KafkaTemplate<String ,AbstractEntity > kafkaTemplate(
		ProducerFactory<String, AbstractEntity> producerFactory
	){
		return new KafkaTemplate<>(producerFactory);
	} //Kafka'ya mesaj göndermek için kullanılacak kafkatemplate i oluşturduk.
	// önceki adımda oluşturulan üretici fabrikasını alarak kafkaTemplate oluşturur.
}
