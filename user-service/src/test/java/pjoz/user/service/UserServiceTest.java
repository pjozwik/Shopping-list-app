package pjoz.user.service;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import pjoz.user.model.MyUserDetails;
import pjoz.user.model.User;
import pjoz.user.model.UserRepository;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSaveUser() {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();

        when(bCryptPasswordEncoder.encode("admin")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.saveUser(user);

        verify(bCryptPasswordEncoder, times(1)).encode("admin");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();

        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(user));

        MyUserDetails userDetails = userService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenLoadUserByUsernameNotFound() {
        when(userRepository.findByUserName("")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(""));
    }

    @Test
    void shouldGetUserByUserName() {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();

        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName("admin");

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUserName());
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        when(userRepository.findByUserName("admin")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName("admin");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldNotDeleteUserWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1);

        assertFalse(result);
        verify(userRepository, never()).deleteById(1);
    }

//    @Test
//    void shouldUpdateUser() {
//
//        User existingUser = User.builder()
//                .userName("admin")
//                .name("admin")
//                .surname("admin")
//                .email("admin@gmail.com")
//                .password("admin")
//                .roles("ADMIN")
//                .id(1)
//                .build();
//
//        User updatedUser = User.builder()
//                .id(1)
//                .name("UpdatedName")
//                .userName("UpdatedName")
//                .surname("UpdatedSurname")
//                .email("updated.email@example.com")
//                .password("updatedPassword")
//                .roles("ADMIN")
//                .build();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
//        when(userRepository.saveAndFlush(any(User.class))).thenReturn(updatedUser);
//
//        Optional<Integer> result = userService.updateUser(updatedUser, 1);
//
//        assertTrue(result.isPresent());
//        assertEquals(1, result.get());
//
//        verify(userRepository, times(1)).findById(1);
//        verify(userRepository, times(1)).saveAndFlush(any(User.class));
//    }

    @Test
    void shouldNotUpdateUserWhenUserNotFound() {
        User updatedUser = User.builder()
                .id(1)
                .name("UpdatedName")
                .userName("UpdatedName")
                .surname("UpdatedSurname")
                .email("updated.email@example.com")
                .password("updatedPassword")
                .roles("ADMIN")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Integer> result = userService.updateUser(updatedUser, 1);

        assertFalse(result.isPresent());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }
}
