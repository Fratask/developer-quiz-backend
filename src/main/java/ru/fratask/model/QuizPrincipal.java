package ru.fratask.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Setter
public class QuizPrincipal extends User {

    private long id;

    public QuizPrincipal(Long id, String username, String password, List<SimpleGrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
