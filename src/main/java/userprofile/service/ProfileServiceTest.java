package userprofile.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import userprofile.exception.ResourceNotFoundException;
import userprofile.model.Profile;
import userprofile.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import userprofile.repository.ProfileRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void createProfile_shouldLinkUserAndSave() {
        User user = new User("Michiel", "Michiel@vandergragt.eu");
        Profile profile = new Profile("I love teaching", null, null);

        when(userService.getUserById(1L)).thenReturn(user);
        when(profileRepository.save(any(Profile.class))).thenAnswer(inv -> inv.getArgument(0));
        Profile result = profileService.createProfile(1L, profile);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getBio()).isEqualTo("I love teaching");
        verify(profileRepository).save(profile);
    }

    @Test
    void createProfile_shouldThrowException_whenUserNotFound() {
        when(userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException("User not found with id: 99"));
        assertThatThrownBy(() -> profileService.createProfile(99L, new Profile()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
        verify(profileRepository, never()).save(any());
    }

    @Test
    void getProfileByUserId_shouldReturnProfile_whenExists() {
        User user = new User("Lillie", "lillie@vandergragt.eu");
        Profile profile = new Profile("Student", null, user);
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        Profile result = profileService.getProfileByUserId(1L);
        assertThat(result.getBio()).isEqualTo("Student");
    }

    @Test
    void getProfileByUserId_shouldThrowException_whenNotFound() {
        when(profileRepository.findByUserId(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> profileService.getProfileByUserId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProfile_shoulUpdateBio() {
        User user = new User("Michiel", "michiel@vandergragt.eu");
        Profile existing = new Profile("Old info", null, user);
        Profile updates = new Profile("New info", null, null);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Profile result = profileService.updateProfile(1L, updates);
        assertThat(result.getBio()).isEqualTo("New info");
    }

}