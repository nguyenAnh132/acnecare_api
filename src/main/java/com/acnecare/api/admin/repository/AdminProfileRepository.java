package com.acnecare.api.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.admin.entity.AdminProfile;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, String> {

}
