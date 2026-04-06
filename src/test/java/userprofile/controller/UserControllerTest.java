package userprofile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import userprofile.exception.ResourceNotFoundException;
import userprofile.model.User;
import userprofile.service.UserService;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_shouldReturn200AndList() throws Exception {
        when(userService.getAllUsers()).thenReturn(
                List.of(new User("Michiel", "michiel@vandergragt.eu"))
        );

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Michiel"))
                .andExpect(jsonPath("$[0].email").value("michiel@vandergragt.eu"));
    }

    @Test
    void getUserById_shouldReturn404_NotFound() throws Exception {
        when (userService.getUserById(99L))
                .thenThrow(new ResourceNotFoundException("User Not Found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not Found with id: 99"));
    }

    @Test
    void createUser_shouldReturn201_whenValidInput() throws Exception {
        User user = new User("Michiel", "michiel@vandergragt.eu");
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Michiel"));
    }

    @Test
    void createUser_shouldReturn400_whenUsernameIsBlank() throws Exception {
        User invalid = new User("", "michiel@vandergragt.eu");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists());
    }

    @Test
    void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {
        User invalid = new User("", "michiel@vandergragt.eu");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
    @Test
            void updateUser_shouldReturn200_whenValid() throws Exception {
            User updated = new User ("Michiel_updated", "michiel@vandergragt.eu");
        // Add this before mockMvc.perform
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updated);

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                            .andExpect(jsonPath("$.username").value("Michiel_updated"));
    }

    @Test
            void deleteUser_shouldReturn204_whenUserExists() throws Exception {
            doNothing().when(userService).deleteUser(1L);
            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isNoContent());
        }
        @Test
                void deleteUser_shouldReturn404_whenUserNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("User not Found with id: 99"))
                    .when(userService).deleteUser(99L);

            mockMvc.perform(delete("/api/users/99"))
                    .andExpect(status().isNotFound());
        }
}
