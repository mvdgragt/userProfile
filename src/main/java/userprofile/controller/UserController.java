package userprofile.controller;
import userprofile.model.User;
import userprofile.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
private final UserService userService;
public UserController(UserService userService) {
    this.userService = userService;
}
@GetMapping
    public List<User> getAllUsers() {
    return userService.getAllUsers();
}

@GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
}

@PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    User created = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

@PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user){
    User updated = userService.updateUser(id, user);
    return ResponseEntity.ok(updated);
}

@DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
}
}


