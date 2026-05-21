package com.pulse.ssl.repository;

import com.pulse.ssl.entity.SslCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SslCheckRepository extends JpaRepository<SslCheck, String> {
    Optional<SslCheck> findTopByServiceIdOrderByCheckedAtDesc(String serviceId);
}