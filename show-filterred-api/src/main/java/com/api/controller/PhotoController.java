package com.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.api.RO.PhotoRO;
import com.api.kafka.producer.PhotosProducer;

@RestController
@RequestMapping(path = "/photos")
public class PhotoController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PhotosProducer photosProducer;

	@GetMapping(path = "/save-photos")
	public String savePhotos() {
		List<PhotoRO> photos = new ArrayList<PhotoRO>();
		String url = "https://jsonplaceholder.typicode.com/photos";
		ResponseEntity<List<PhotoRO>> photosResponse = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<PhotoRO>>() {
				});
		if (photosResponse.hasBody()) {
			photos = photosResponse.getBody();
			for (PhotoRO photo : photos) {
				photosProducer.produce(photo);
			}
		}
		return photos.size() + " entries received.";
	}
}
