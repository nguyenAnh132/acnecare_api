package com.acnecare.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByRoles_Name(String roleName);
}
