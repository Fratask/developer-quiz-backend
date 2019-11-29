package ru.fratask.service;

import ru.fratask.model.dto.AuthenticationRequest;
import ru.fratask.model.dto.AuthenticationResponse;

public interface AuthService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest);

    void authenticate(String username, String password);
}
