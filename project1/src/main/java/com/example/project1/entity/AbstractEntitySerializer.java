package com.example.project1.entity;

import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

//bu metot verilerimizi serileştirmek için kullanıyoruz. StringSerializer işimize yaramadığı için kendi Serializer metodumuzu oluşturduk
public class AbstractEntitySerializer implements Serializer<AbstractEntity> {
	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Java nesnelerini Json verilerine (ya da tam tersi) dönüştürmek için kullanılır
// javaTimeModule LocalDateTime gibi tarih ve zaman türlerinin doğru şekilde işlenmesini sağlar.
	@Override
	public byte[] serialize(String topic, AbstractEntity data) {
		try {
			return objectMapper.writeValueAsBytes(data); //bu metot AbstractEntity nesnesini JSON formatına dönüştürerek serileştirir
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
