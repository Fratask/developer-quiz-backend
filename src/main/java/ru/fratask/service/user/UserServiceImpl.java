package ru.fratask.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fratask.model.Message;
import ru.fratask.model.entity.RegisterLink;
import ru.fratask.model.entity.Role;
import ru.fratask.model.entity.User;
import ru.fratask.model.entity.UserRole;
import ru.fratask.model.exception.QuizException;
import ru.fratask.model.exception.QuizExceptionResponse;
import ru.fratask.repository.RegisterLinkRepository;
import ru.fratask.repository.RoleRepository;
import ru.fratask.repository.UserRepository;
import ru.fratask.repository.UserRoleRepository;
import ru.fratask.service.mail.MailService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${apiUrl}")
    private String apiUrl;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterLinkRepository registerLinkRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RegisterLinkRepository registerLinkRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.registerLinkRepository = registerLinkRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public User register(String username, String password, String email) {
        userRepository.findByUsername(username).ifPresent((user -> {
            throw new QuizException(QuizExceptionResponse.USER_ALREADY_EXISTS);
        }));

        User savedUser = userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .build()
        );
        setRole(savedUser, Role.builder().name("USER").build());
        mailService.send(email, Message.REGISTRATION_TITLE_MESSAGE.getMessage(), Message.REGISTRATION_BODY_MESSAGE.getMessage() + generateRegistrationLink(email));
        return savedUser;

    }

    @Override
    public User load(User user) {
        User loadedUser;
        if (user.getId() != null && user.getUsername() != null) {
            loadedUser = userRepository.findById(user.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
            if (user.getUsername().equals(loadedUser.getUsername())) {
                return loadedUser;
            }
            throw new QuizException(QuizExceptionResponse.USER_NOT_FOUND);
        } else if (user.getId() != null) {
            return userRepository.findById(user.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
        } else if (user.getUsername() != null) {
            return userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
        }
        throw new QuizException(QuizExceptionResponse.USER_NOT_FOUND);
    }

    @Override
    public User update(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
        if (updatedUser.getUsername().equals(user.getUsername())) {
            return userRepository.save(user);
        }
        throw new QuizException(QuizExceptionResponse.USER_UPDATE_FAIL);
    }

    @Override
    public User delete(User user) {
        User deletedUser;
        if (user.getId() != null && user.getUsername() != null) {
            deletedUser = userRepository.findById(user.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
            if (user.getUsername().equals(deletedUser.getUsername())) {
                userRepository.delete(deletedUser);
                return deletedUser;
            }
            throw new QuizException(QuizExceptionResponse.USER_NOT_FOUND);
        } else if (user.getId() != null) {
            deletedUser = userRepository.findById(user.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
            userRepository.delete(deletedUser);
            return deletedUser;
        } else if (user.getUsername() != null) {
            deletedUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
            userRepository.delete(deletedUser);
            return deletedUser;
        }
        throw new QuizException(QuizExceptionResponse.USER_NOT_FOUND);
    }

    @Override
    public UserRole setRole(User user, Role role) {
        User foundUser = load(user);
        Role foundRole = loadRole(role);
        return userRoleRepository.save(UserRole.builder()
                .userId(foundUser.getId())
                .roleId(foundRole.getId())
                .build());
    }

    @Override
    public void confirm(String email, String code) {
        RegisterLink registerLink = registerLinkRepository.findByEmail(email).orElseThrow(() -> new QuizException(QuizExceptionResponse.CONFIRM_REGISTRATION_ERROR));
        if (!registerLink.getCode().equals(code) || LocalDateTime.now().isAfter(registerLink.getExpirationDate()) || registerLink.getUsed()) {
            throw new QuizException(QuizExceptionResponse.CONFIRM_REGISTRATION_ERROR);
        }
        registerLink.setUsed(true);
        registerLinkRepository.save(registerLink);
        mailService.send(email, Message.REGISTRATION_COMPLETE_TITLE_MESSAGE.getMessage(), Message.REGISTRATION_COMPLETE_BODY_MESSAGE.getMessage());
    }

    private Role loadRole(Role role) {
        Role loadedRole;
        if (role.getId() != null && role.getName() != null) {
            loadedRole = roleRepository.findById(role.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.ROLE_NOT_FOUND));
            if (role.getName().equals(loadedRole.getName())) {
                return loadedRole;
            }
            throw new QuizException(QuizExceptionResponse.USER_NOT_FOUND);
        } else if (role.getId() != null) {
            return roleRepository.findById(role.getId()).orElseThrow(() -> new QuizException(QuizExceptionResponse.ROLE_NOT_FOUND));
        } else if (role.getName() != null) {
            return roleRepository.findByName(role.getName()).orElseThrow(() -> new QuizException(QuizExceptionResponse.ROLE_NOT_FOUND));
        }
        throw new QuizException(QuizExceptionResponse.ROLE_NOT_FOUND);
    }

    private String generateRegistrationLink(String email) {
        String code = UUID.randomUUID() + Encryptors.text(apiUrl, KeyGenerators.string().generateKey()).encrypt(email);
        registerLinkRepository.save(RegisterLink.builder()
                .email(email)
                .code(code)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .build());
        return apiUrl + "/user/confirm?email=" + email + "&code=" + code;
    }
}
