package ru.fratask.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "register_links")
public class RegisterLink {

    /**
     * Registration user's email
     */
    @Column(name = "email")
    private String email;

    /**
     * Confirmation code
     */
    @Column(name = "code")
    private String code;

    /**
     * Used info
     */
    @Column(name = "used")
    private Boolean used;

    /**
     * Expiration date
     */
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
