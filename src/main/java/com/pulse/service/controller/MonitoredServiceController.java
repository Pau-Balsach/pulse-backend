package com.pulse.service.controller;

import com.pulse.service.dto.ServiceRequest;
import com.pulse.service.dto.ServiceResponse;
import com.pulse.service.service.MonitoredServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/services")
@RequiredArgsConstructor
public class MonitoredServiceController {

    private final com.pulse.monitor.repository.MonitorCheckRepository checkRepository;
    private final MonitoredServiceService monitoredServiceService;

    @PostMapping
    public ResponseEntity<ServiceResponse> create(
            @PathVariable String projectId,
            @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(monitoredServiceService.create(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAll(@PathVariable String projectId) {
        return ResponseEntity.ok(monitoredServiceService.getByProject(projectId));
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> delete(@PathVariable String serviceId) {
        monitoredServiceService.delete(serviceId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable String serviceId,
            @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(monitoredServiceService.update(serviceId, request));
    }

    @GetMapping("/{serviceId}/checks")
    public ResponseEntity<List<com.pulse.monitor.entity.MonitorCheck>> getChecks(
            @PathVariable String serviceId) {
        return ResponseEntity.ok(checkRepository.findTop50ByServiceIdOrderByCheckedAtAsc(serviceId));
    }
}