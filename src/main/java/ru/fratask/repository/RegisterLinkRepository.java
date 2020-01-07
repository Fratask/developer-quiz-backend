package ru.fratask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fratask.model.entity.RegisterLink;

import java.util.Optional;

@Repository
public interface RegisterLinkRepository extends JpaRepository<RegisterLink, Long> {

    @Query(value = "select * from register_links where email = :email", nativeQuery = true)
    Optional<RegisterLink> findByEmail(@Param("email") String email);

}
