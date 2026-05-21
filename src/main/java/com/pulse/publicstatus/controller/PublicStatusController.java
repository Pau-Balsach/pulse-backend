package com.pulse.publicstatus.controller;

import com.pulse.publicstatus.dto.PublicStatusResponse;
import com.pulse.publicstatus.service.PublicStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/status")
@RequiredArgsConstructor
public class PublicStatusController {

    private final PublicStatusService publicStatusService;

    @GetMapping("/{projectId}")
    public ResponseEntity<PublicStatusResponse> getStatus(@PathVariable String projectId) {
        return ResponseEntity.ok(publicStatusService.getStatus(projectId));
    }
}