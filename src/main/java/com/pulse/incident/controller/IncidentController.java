package com.pulse.incident.controller;

import com.pulse.incident.entity.Incident;
import com.pulse.incident.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceId}/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<Incident>> getIncidents(@PathVariable String serviceId) {
        return ResponseEntity.ok(incidentService.getByServiceId(serviceId));
    }
}