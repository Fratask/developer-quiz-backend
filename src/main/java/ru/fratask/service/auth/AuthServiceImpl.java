package ru.fratask.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.fratask.model.dto.AuthenticationRequest;
import ru.fratask.model.dto.AuthenticationResponse;
import ru.fratask.model.exception.QuizException;
import ru.fratask.model.exception.QuizExceptionResponse;
import ru.fratask.security.QuizUserDetailsService;
import ru.fratask.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final QuizUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(QuizUserDetailsService userDetailsService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(token);
    }

    @Override
    public void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new QuizException(QuizExceptionResponse.USER_DISABLED);
        } catch (BadCredentialsException e) {
            throw new QuizException(QuizExceptionResponse.INVALID_CREDENTIALS);
        }
    }
}
