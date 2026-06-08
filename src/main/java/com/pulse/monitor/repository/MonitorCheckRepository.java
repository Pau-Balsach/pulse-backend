package com.pulse.monitor.repository;

import com.pulse.monitor.entity.MonitorCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitorCheckRepository extends JpaRepository<MonitorCheck, String> {
    List<MonitorCheck> findTop20ByServiceIdOrderByCheckedAtDesc(String serviceId);
    List<MonitorCheck> findTop50ByServiceIdOrderByCheckedAtAsc(String serviceId);
    List<MonitorCheck> findTop500ByServiceIdOrderByCheckedAtDesc(String serviceId);
}