package com.example.sampleproject.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.sampleproject.datastore.FakeUserProfileDataStore;

@Repository
public class UserProfileFileDataAcessService {

	private final FakeUserProfileDataStore fakeUserProfileDataStore;

	@Autowired
	public UserProfileFileDataAcessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
		this.fakeUserProfileDataStore = fakeUserProfileDataStore;
	}

	List<UserProfile> getUserProfiles() {
		return fakeUserProfileDataStore.getUserProfiles();
	}

}
