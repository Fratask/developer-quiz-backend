package ru.fratask.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    private Long id;

    private Long userId;
    private Long roleId;
}
