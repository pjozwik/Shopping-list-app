package pjoz.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pjoz.user.model.User;
import pjoz.user.model.UserRepository;
import pjoz.user.service.AuthenticationService;
import pjoz.user.service.UserService;
import pjoz.user.util.JwtUtil;

import javax.validation.constraints.DecimalMax;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldCreateNewUser() throws Exception {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been successfully added"));

        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void shouldGetUserById() throws Exception {
        int userId = 1;
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk());


        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnNotFoundForInvalidUserId() throws Exception {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userRepository, times(1)).findById(userId);
    }

//    @Test
//    void shouldGetLoggedInUserDetails() throws Exception {
//        Authentication authentication = mock(Authentication.class);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("loggedInUser");
//
//        User loggedInUser = new User(/* provide necessary data */);
//        when(userService.getUserByUserName("loggedInUser")).thenReturn(Optional.of(loggedInUser));
//
//        mockMvc.perform(get("/api/users/loggedUser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("loggedInUser"));
//
//        verify(userService, times(1)).getUserByUserName("loggedInUser");
//    }

    @Test
    void shouldGetAllUsers() throws Exception {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnNoContentForEmptyUserList() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isNoContent());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteUser() throws Exception {
        int userId = 1;
        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(userId)));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {
        int userId = 1;
        when(userService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        int userId = 1;
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();
        when(userService.updateUser(user, userId)).thenReturn(Optional.of(userId));

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(userId)));

        verify(userService, times(1)).updateUser(user, userId);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingUser() throws Exception {
        int userId = 1;
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();
        when(userService.updateUser(user, userId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(user, userId);
    }
}
