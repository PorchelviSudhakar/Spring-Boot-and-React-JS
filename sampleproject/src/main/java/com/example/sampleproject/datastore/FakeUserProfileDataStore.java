package com.example.sampleproject.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.sampleproject.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {
	
	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();
	
	static {
//		USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"James",null));
		USER_PROFILES.add(new UserProfile(UUID.fromString("8666bed2-a723-4a62-9eb0-dbcc14caa74f"),"James",null));		
		USER_PROFILES.add(new UserProfile(UUID.fromString("66cb9c8a-3b4b-4d9a-803b-133020f7c1c6"),"Thomas",null));
	}
	
	public List<UserProfile> getUserProfiles(){
		return USER_PROFILES;
	}

}
