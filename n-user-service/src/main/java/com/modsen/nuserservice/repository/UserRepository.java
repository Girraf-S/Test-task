package com.modsen.nuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modsen.nuserservice.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "update users set enabled=true where id=:id", nativeQuery = true)
    void activateUserById(Long id);

    @Modifying
    @Query(value = "update users set enabled=false where id=:id", nativeQuery = true)
    void deactivateUserById(Long id);
}
