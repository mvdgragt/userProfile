package userprofile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userprofile.exception.ResourceNotFoundException;
import userprofile.model.Profile;
import userprofile.repository.ProfileRepository;
import userprofile.model.User;

import java.util.List;

@Service
@Transactional

public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserService userService;

 public ProfileService(ProfileRepository profileRepository, UserService userService) {
     this.profileRepository = profileRepository;
     this.userService = userService;
 }
 @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
     return profileRepository.findAll();
 }

 @Transactional(readOnly = true)
    public Profile getProfileById(Long id) {
     return profileRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user with id: " + id));
 }

 @Transactional(readOnly = true)
 public Profile getProfileByUserId(Long userId) {
     return profileRepository.findByUserId(userId)
             .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user with id: " + userId));
 }

 public Profile createProfile(Long userId, Profile profile) {
     User user = userService.getUserById(userId);
     profile.setUser(user);
     return profileRepository.save(profile);
 }

    public Profile updateProfile(Long userId, Profile updatedData) {
        Profile existing = getProfileById(userId);
        existing.setBio(updatedData.getBio());
        existing.setAvatarUrl(updatedData.getAvatarUrl());
        return profileRepository.save(existing);
    }

 public void deleteProfile(Long id) {
     getProfileById(id);
     profileRepository.deleteById(id);
 }
}
