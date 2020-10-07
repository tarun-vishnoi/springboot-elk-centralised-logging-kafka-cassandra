package com.api.builder;

import org.springframework.stereotype.Component;

import com.api.RO.PhotoRO;
import com.api.pojo.Photo;

@Component
public class PhotosBuilder {

	public Photo convert2CassandraEntity(PhotoRO photoRO) {
		Photo photo = new Photo();
		photo.setId(photoRO.getId());
		photo.setAlbumId(photoRO.getAlbumId());
		photo.setTitle(photoRO.getTitle());
		photo.setUrl(photoRO.getUrl());
		photo.setThumbnailUrl(photoRO.getThumbnailUrl());
		return photo;
	}

	public com.api.doc.Photo convert2ElasticDoc(PhotoRO photoRO) {
		com.api.doc.Photo photo = new com.api.doc.Photo();
		photo.setId(photoRO.getId());
		photo.setAlbumId(photoRO.getAlbumId());
		photo.setTitle(photoRO.getTitle());
		photo.setUrl(photoRO.getUrl());
		photo.setThumbnailUrl(photoRO.getThumbnailUrl());
		return photo;
	}
}
