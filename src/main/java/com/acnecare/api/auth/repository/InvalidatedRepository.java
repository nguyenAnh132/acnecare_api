package com.acnecare.api.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.auth.entity.InvalidatedToken;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {
    List<InvalidatedToken> findAllByExpiryTimeBefore(Date now);
}
