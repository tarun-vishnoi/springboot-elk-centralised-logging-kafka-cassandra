package com.api.service;

import com.api.doc.Photo;

public interface PhotosService {

	public void savePhotos2Elastic(Photo photo);

	public void savePhoto2Cassandra(com.api.pojo.Photo photo);
}
