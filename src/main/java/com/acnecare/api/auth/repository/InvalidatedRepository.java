package com.acnecare.api.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.auth.entity.InvalidatedToken;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {

}
