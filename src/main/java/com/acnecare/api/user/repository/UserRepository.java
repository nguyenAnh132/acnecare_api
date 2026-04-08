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

    Optional<User> findByGoogleSub(String googleSub);

    List<User> findAllByGoogleSub(String googleSub);

    List<User> findAllByEmail(String email);

    List<User> findByRoles_Name(String roleName);

    Optional<User> findFirstByRoles_NameAndStatusOrderByCreatedAtAsc(String roleName, String status);

    Optional<User> findFirstByRoles_NameAndIdNotAndStatusOrderByCreatedAtAsc(String roleName, String excludedId,
            String status);
}
