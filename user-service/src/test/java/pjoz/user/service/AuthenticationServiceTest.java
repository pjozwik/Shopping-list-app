package pjoz.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import pjoz.user.model.AuthenticationRequest;
import pjoz.user.model.AuthenticationResponse;
import pjoz.user.model.MyUserDetails;
import pjoz.user.model.User;
import pjoz.user.util.JwtUtil;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;
    @Test
    void shouldAuthenticate() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("admin", "admin");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token", "ADMIN");

        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();

        MyUserDetails userDetails = new MyUserDetails(user);

        when(userService.loadUserByUsername(request.getUserName())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");

        AuthenticationResponse response = authenticationService.authenticate(request);


        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        verify(userService, times(1)).loadUserByUsername(request.getUserName());
        verify(jwtUtil, times(1)).generateToken(userDetails);

        assertNotNull(response);
        assertEquals(response.getRole(), expectedResponse.getRole());
        assertEquals(response.getToken(), expectedResponse.getToken());
    }
}
