package userprofile.controller;
import userprofile.model.Profile;
import userprofile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")

public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public  Profile getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @GetMapping("/user/{userId}")
    public Profile getProfileByUserId(@PathVariable Long userId) {
        return profileService.getProfileByUserId(userId);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Profile> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody Profile profile) {
        Profile created = profileService.createProfile(userId, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
