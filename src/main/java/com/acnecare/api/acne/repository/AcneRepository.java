package com.acnecare.api.acne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acnecare.api.acne.entity.Acne;
import java.util.Optional;

@Repository
public interface AcneRepository extends JpaRepository<Acne, String> {
    Optional<Acne> findByCodeName(String codeName);
}