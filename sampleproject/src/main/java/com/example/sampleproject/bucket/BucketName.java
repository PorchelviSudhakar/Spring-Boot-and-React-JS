package com.example.sampleproject.bucket;

public enum BucketName {
	
	PROFILE_IMAGE("myawsbucket-imageupload-123");
	private final String bucketName;

	BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}
	
}
