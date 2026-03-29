package com.acnecare.api.brand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.brand.entity.BrandProfile;

@Repository
public interface BrandProfileRepository extends JpaRepository<BrandProfile, String> {   

}
