package com.example.sampleproject.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.sampleproject.bucket.BucketName;
import com.example.sampleproject.filestore.FileStore;

@Service
public class UserProfileService {
	
	private final UserProfileFileDataAcessService userProfileDataAcessService;
	private final FileStore filestore;

	@Autowired
	public UserProfileService(UserProfileFileDataAcessService userProfileDataAcessService,FileStore filestore) {
		this.userProfileDataAcessService = userProfileDataAcessService;
		this.filestore = filestore;
	}
	
	
	List<UserProfile> getUserProfiles() {
		return userProfileDataAcessService.getUserProfiles();
	}


	void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
		// check if image is not empty
		if(file.isEmpty()) {
			throw new IllegalStateException(" cannot upload empty file ["+file.getSize() +"]");
		}
		// if file is an image
		System.out.println("file.getContentType()==="+file.getContentType());
		System.out.println("ContentType.IMAGE_JPEG.getMimeType()"+ContentType.IMAGE_JPEG.getMimeType());
		if(!ContentType.IMAGE_JPEG.getMimeType().
				contains(file.getContentType())) {
			throw new IllegalStateException(" File must be an Image ["+file.getSize()+"]");
		}
		// The user exists in our database
		UserProfile user = getUserProfile(userProfileId);
		// Grab some metadata from file if any
		Map<String, String> metadata = extractMetaData(file);
		// store the image in s3 and update database(userProfileImageLink) with S3 image link
		
		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(),user.getUserProfileId());
		String filename = String.format("%s-%s", file.getOriginalFilename(),UUID.randomUUID());
		
		try {
			filestore.save(path, filename, Optional.of(metadata), file.getInputStream());
			user.setUserProfileImageLink(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(e);
		}
		
		
		
		
		
	}


	private Map<String, String> extractMetaData(MultipartFile file) {
		Map<String,String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}


	private UserProfile getUserProfile(UUID userProfileId) {
		UserProfile user = userProfileDataAcessService.getUserProfiles()
		.stream()
		.filter(userprofile -> userprofile.getUserProfileId().equals(userProfileId))
		.findFirst()
		.orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
		
		System.out.println("============user============"+user.getUserProfileId());
		return user;
	}


	public byte[] downloadUserProfileImage(UUID userProfileId) {
		UserProfile user = getUserProfile(userProfileId);
		String path = String.format("%s/%s", 
				BucketName.PROFILE_IMAGE.getBucketName(),
				user.getUserProfileId());
		
		return user.getUserProfileImageLink()
		    .map(key -> filestore.download(path, key))
		    .orElse(new byte[0]);

	}


}
