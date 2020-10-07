package com.api.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.api.RO.PhotoRO;

@Component
public class PhotosProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhotosProducer.class);

	@Autowired
	private KafkaTemplate<String, PhotoRO> kafkaTemplate;

	public void produce(PhotoRO photo) {
		kafkaTemplate.send("topic_photos", photo);
	}

}
