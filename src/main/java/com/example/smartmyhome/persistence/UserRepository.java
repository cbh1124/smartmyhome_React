package com.example.smartmyhome.persistence;


import com.example.smartmyhome.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);
    Boolean existsByUsername(String username);

    Optional<UserEntity> findByTemporary(String temporary);

}
