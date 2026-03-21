package com.acnecare.api.role.reposity;

import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.role.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleReposity extends JpaRepository<Role, String> {
    boolean existsByName(String name);
}
