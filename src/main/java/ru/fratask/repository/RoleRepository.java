package ru.fratask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fratask.model.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r FROM roles r JOIN user_roles ur ON r.id = ur.role_id AND ur.user_id =: userId", nativeQuery = true)
    List<Role> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT r FROM roles r WHERE r.name = :name", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);
}
