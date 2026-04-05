package userprofile.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import userprofile.exception.ResourceNotFoundException;
import userprofile.model.User;
import userprofile.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.times;




@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        //Arrange
        List<User> users = List.of(
                new User("Michiel", "michiel@vandergragt.eu"),
                new User("Hanna", "hanna@vandergragt.eu")
        );
        //Act
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        //Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("Michiel");
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User("Michiel", "michiel@vandergragt.eu");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getUsername()).isEqualTo("Michiel");
        verify(userRepository, times(1)).findById(1L);
    }

@Test
    void getUserById_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
}

@Test
void createUser_shouldSaveAndReturnUser() {
    User user = new User("Emma", "emma@vandergragt.eu");
    when(userRepository.save(user)).thenReturn(user);

    User result = userService.createUser(user);

    assertThat(result.getEmail()).isEqualTo("emma@vandergragt.eu");
    verify(userRepository).save(user);
}
    @Test
            void updateUser_shouldUpdateUsernameAndEmail() {
        User existing = new User("Emma", "emma@vandergragt.eu");
        User updates = new User("Emma_updated", "emma_updated@vandergragt.eu");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(1L, updates);
        assertThat(result.getUsername()).isEqualTo("Emma_updated");
        assertThat(result.getEmail()).isEqualTo("emma_updated@vandergragt.eu");
    }
        @Test
        void updateUser_shouldThrowException_whenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.updateUser(99L, new User("x", "x@x.se")))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void deleteUser_shouldCallDeleteById_whenUserExists() {
            User user = new User("Michiel", "michiel@vandergragt.eu");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            userService.deleteUser(1L);

            verify(userRepository, never()).delete(any());
            verify(userRepository).deleteById(1L);
    }
    @Test
void deleteUser_shouldThrowException_whenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.deleteUser(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
            verify(userRepository, never()).deleteById(any());
    }

}