package com.consumer.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.consumer.util.Constant;

@Configuration
public class KafkaTweetsConfig {

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		// basic producer properties
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// safe producer
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		config.put(ProducerConfig.ACKS_CONFIG, "all");

		config.put(ProducerConfig.RETRIES_CONFIG, 10);
		config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
		config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

		// high throughput producer
		config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		config.put(ProducerConfig.LINGER_MS_CONFIG, "20");
		config.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32kb batch file

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<String, String>(producerFactory());
	}

	@Bean
	public void createTopicBean() throws InterruptedException, ExecutionException {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.BOOTSTRAP_SERVERS_CONFIG);
		AdminClient admin = AdminClient.create(config);
		ListTopicsResult listTopics = admin.listTopics();
		Set<String> names = listTopics.names().get();
		boolean contains = names.contains("tarun_topic");
		if (!contains) {
			List<NewTopic> topicList = new ArrayList<NewTopic>();
			int partitions = 12;
			Short replication = 1;
			NewTopic newTopic = new NewTopic("tarun_topic", partitions, replication);
			topicList.add(newTopic);
			admin.createTopics(topicList);
		} else {
			System.out.println(names.toString());
		}
	}
}
