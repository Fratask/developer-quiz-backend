package ru.fratask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fratask.model.dto.user.RegisterRequest;
import ru.fratask.model.entity.UserRole;
import ru.fratask.service.user.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        userService.register(registerRequest.getUsername(), registerRequest.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity setRole(@RequestBody UserRole userRole) {
        userService.setRole(userRole.getUser(), userRole.getRole());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
