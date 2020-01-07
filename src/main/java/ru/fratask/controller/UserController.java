package ru.fratask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fratask.model.dto.user.RegisterRequest;
import ru.fratask.model.entity.UserRole;
import ru.fratask.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ResponseEntity confirm(@RequestParam String email, @RequestParam String code) {
        userService.confirm(email, code);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity setRole(@RequestBody UserRole userRole) {
        userService.setRole(userRole.getUser(), userRole.getRole());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
