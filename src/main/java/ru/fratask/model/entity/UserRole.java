package ru.fratask.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
public class UserRole {

    /**
     * UserRole id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * UserRole user by id
     */
    @Column(name = "userId")
    private Long userId;

    /**
     * UserRole role by id
     */
    @Column(name = "roleId")
    private Long roleId;

    /**
     * UserRole user
     */
    @Transient
    private User user;

    /**
     * UserRole role
     */
    @Transient
    private Role role;
}
