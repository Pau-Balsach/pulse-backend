package com.pulse.incident.repository;

import com.pulse.incident.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, String> {
    Optional<Incident> findTopByServiceIdAndStatusOrderByStartedAtDesc(String serviceId, String status);
    List<Incident> findByServiceIdOrderByStartedAtDesc(String serviceId);
}