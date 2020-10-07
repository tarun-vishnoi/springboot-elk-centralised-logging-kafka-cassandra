package com.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.api.RO.PhotoRO;
import com.api.serializer.JavaSerializer;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, String> producerFactory4Tweets() {
		Map<String, Object> config = new HashMap<>();

		// basic producer properties
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// safe producer
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		config.put(ProducerConfig.ACKS_CONFIG, "all");
		config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

		// high throughput producer
		config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		config.put(ProducerConfig.LINGER_MS_CONFIG, "20");
		config.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32kb batch file

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate4Tweets() {
		return new KafkaTemplate<String, String>(producerFactory4Tweets());
	}

	@Bean
	public ProducerFactory<String, PhotoRO> producerFactory4Photos() {
		Map<String, Object> config = new HashMap<>();

		// basic producer properties
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JavaSerializer.class);

		// safe producer
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		config.put(ProducerConfig.ACKS_CONFIG, "all");
		config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

		// high throughput producer
		config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		config.put(ProducerConfig.LINGER_MS_CONFIG, "20");
		config.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32kb batch file

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, PhotoRO> kafkaTemplate4Photos() {
		return new KafkaTemplate<String, PhotoRO>(producerFactory4Photos());
	}

}
