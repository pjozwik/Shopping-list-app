package pjoz.user.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pjoz.user.model.MyUserDetails;
import pjoz.user.model.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String secret = "6dmZwy8/uPHGfbbwVY2CMmi7pJdCPNFJIJvcP2+FkoY=";

    private String token;
    private MyUserDetails userDetails;
    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userName("admin")
                .name("admin")
                .surname("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles("ADMIN")
                .id(1)
                .build();
        userDetails = new MyUserDetails(user);
        token = jwtUtil.generateToken(userDetails);
    }
    @Test
    void testExtractUsername() {
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals("admin", extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        Date extractedExpiration = jwtUtil.extractExpiration(token);
        assertNotNull(extractedExpiration);
    }

    @Test
    void testExtractClaim() {
        Claims claims = jwtUtil.extractAllClaims(token);
        String extractedUsername = jwtUtil.extractClaim(token, Claims::getSubject);
        assertEquals("admin", extractedUsername);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtUtil.generateToken(userDetails);
        assertNotNull(generatedToken);
    }

    @Test
    void testValidateToken() {
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertTrue(isValid);
    }
}