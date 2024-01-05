package pjoz.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pjoz.user.model.AuthenticationRequest;
import pjoz.user.model.AuthenticationResponse;
import pjoz.user.service.AuthenticationService;
import pjoz.user.service.UserService;
import pjoz.user.util.JwtUtil;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "admin");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token", "role");
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(expectedResponse);

        String json = objectMapper.writeValueAsString(authenticationRequest);

        mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).authenticate(any());
    }
}
