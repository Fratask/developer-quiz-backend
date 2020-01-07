package ru.fratask.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fratask.model.Message;
import ru.fratask.model.entity.Role;
import ru.fratask.model.entity.User;
import ru.fratask.model.entity.UserRole;
import ru.fratask.model.exception.QuizException;
import ru.fratask.model.exception.QuizExceptionResponse;
import ru.fratask.repository.RoleRepository;
import ru.fratask.repository.UserRepository;
import ru.fratask.repository.UserRoleRepository;
import ru.fratask.service.mail.MailService;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${apiUrl}")
    private String apiUrl;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    private String generateRegistrationLink(String key) {
        return apiUrl + UUID.randomUUID() + Encryptors.text(apiUrl, KeyGenerators.string().generateKey()).encrypt(key);
    }
}
