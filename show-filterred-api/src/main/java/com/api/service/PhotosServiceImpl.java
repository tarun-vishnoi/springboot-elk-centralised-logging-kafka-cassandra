package com.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.doc.Photo;
import com.api.repo.PhotoCassandraRepo;
import com.api.repo.PhotosRepo;

@Service
public class PhotosServiceImpl implements PhotosService {

	@Autowired
	private PhotosRepo photosRepo;

	@Autowired
	private PhotoCassandraRepo photoCassandraRepo;

	@Override
	public void savePhotos2Elastic(Photo photo) {
		photosRepo.save(photo);
	}

	@Override
	public void savePhoto2Cassandra(com.api.pojo.Photo photo) {
		photoCassandraRepo.save(photo);
	}
}
