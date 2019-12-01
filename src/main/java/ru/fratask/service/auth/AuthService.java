package ru.fratask.service.auth;

import ru.fratask.model.dto.AuthenticationRequest;
import ru.fratask.model.dto.AuthenticationResponse;

public interface AuthService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest);

    void authenticate(String username, String password);
}