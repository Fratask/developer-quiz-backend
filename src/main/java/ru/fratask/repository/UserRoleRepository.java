package ru.fratask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fratask.model.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
