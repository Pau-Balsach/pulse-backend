package com.pulse.service.repository;

import com.pulse.service.entity.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, String> {
    List<MonitoredService> findByProjectId(String projectId);
    List<MonitoredService> findByActiveTrue();
    List<MonitoredService> findByProjectIdOrderByNameAsc(String projectId);
}