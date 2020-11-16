package com.example.sampleproject.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.apache.http.impl.io.IdentityOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class FileStore {

	private final AmazonS3 s3;

	@Autowired
	public FileStore(AmazonS3 s3) {
		this.s3 = s3;
	}

	// path = path of bucket and underlying folders
	// filename = file that you want to upload
	// optionalmetadata = contenttype,length
	public void save(String path, String fileName, Optional<Map<String, String>> optionalMetadata,
			InputStream inputStream) {
		System.out.println("Inside Save===================");
		ObjectMetadata metadata = new ObjectMetadata();
		optionalMetadata.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(metadata::addUserMetadata);
			}
		});
		try {

			s3.putObject(path, fileName, inputStream, metadata);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException(" Failed to store File to S3", e);
		}

	}

	public byte[] download(String path,String key) {
		try {
			com.amazonaws.services.s3.model.S3Object object = s3.getObject(path, key);
			S3ObjectInputStream inputStream = object.getObjectContent();
	        return IOUtils.toByteArray(inputStream);
		} catch (AmazonServiceException  |	IOException e) {
			throw new IllegalStateException(" Failed to store File to S3", e);
		}
	}

}
