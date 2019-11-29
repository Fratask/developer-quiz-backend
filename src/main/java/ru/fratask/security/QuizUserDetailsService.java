package ru.fratask.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fratask.model.QuizPrincipal;
import ru.fratask.model.entity.User;
import ru.fratask.model.exception.QuizException;
import ru.fratask.model.exception.QuizExceptionResponse;
import ru.fratask.repository.RoleRepository;
import ru.fratask.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public QuizUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new QuizException(QuizExceptionResponse.USER_NOT_FOUND));
        List<SimpleGrantedAuthority> authorities = roleRepository.findByUserId(user.getId())
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(toList());
        return new QuizPrincipal(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }
}
