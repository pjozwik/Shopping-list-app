package pjoz.user.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pjoz.user.model.AuthenticationRequest;
import pjoz.user.model.MyUserDetails;
import pjoz.user.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public String authenticate(AuthenticationRequest request){
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        final MyUserDetails user = userService.loadUserByUsername(request.getUserName());
        final String token = jwtUtil.generateToken(user);
        return token;
    }
}
