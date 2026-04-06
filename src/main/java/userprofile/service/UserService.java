package userprofile.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import userprofile.exception.ResourceNotFoundException;
import userprofile.model.User;
import userprofile.repository.UserRepository;

import java.util.List;

@Service
@Transactional

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly=true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly=true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedData) {
        User existing = getUserById(id);
        existing.setUsername(updatedData.getUsername());
        existing.setEmail(updatedData.getEmail());
        return userRepository.save(existing);
    }
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }
}
