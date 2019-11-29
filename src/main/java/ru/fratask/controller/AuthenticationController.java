package ru.fratask.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.model.dto.AuthenticationRequest;
import ru.fratask.service.AuthService;

@RestController
@RequestMapping("/oauth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authService.createAuthenticationToken(authenticationRequest));
    }
}