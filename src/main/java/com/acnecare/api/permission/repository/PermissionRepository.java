package com.acnecare.api.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.permission.entity.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
