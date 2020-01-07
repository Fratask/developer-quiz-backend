package ru.fratask.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    /**
     * User id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * User username
     */
    @Column(name = "username")
    private String username;

    /**
     * User password
     */
    @Column(name = "password")
    private String password;


    /**
     * User last login time
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * User email address
     */
    @Column(name = "email")
    private String email;

    /**
     * User phone number
     */
    @Column(name = "phone")
    private String phone;
}
