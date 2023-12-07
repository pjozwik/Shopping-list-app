package pjoz.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pjoz.user.model.AuthenticationRequest;
import pjoz.user.model.AuthenticationResponse;
import pjoz.user.service.AuthenticationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
