package com.pulse.service.service;

import com.pulse.service.dto.ServiceRequest;
import com.pulse.service.dto.ServiceResponse;
import com.pulse.service.entity.MonitoredService;
import com.pulse.service.repository.MonitoredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoredServiceService {

    private final MonitoredServiceRepository repository;

    public ServiceResponse create(String projectId, ServiceRequest request) {
        MonitoredService service = MonitoredService.builder()
                .projectId(projectId)
                .name(request.getName())
                .url(request.getUrl())
                .method(request.getMethod())
                .expectedStatus(request.getExpectedStatus())
                .timeoutMs(request.getTimeoutMs())
                .checkIntervalSeconds(request.getCheckIntervalSeconds())
                .apiKeyHeader(request.getApiKeyHeader())
                .apiKeyValue(request.getApiKeyValue())
                .build();

        return toResponse(repository.save(service));
    }

    public List<ServiceResponse> getByProject(String projectId) {
        return repository.findByProjectIdOrderByNameAsc(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(String serviceId) {
        repository.deleteById(serviceId);
    }

    private ServiceResponse toResponse(MonitoredService s) {
        return new ServiceResponse(
                s.getId(), s.getProjectId(), s.getName(), s.getUrl(),
                s.getMethod(), s.getExpectedStatus(), s.getTimeoutMs(),
                s.getCheckIntervalSeconds(), s.getActive(), s.getCreatedAt(),
                s.getApiKeyHeader(), s.getApiKeyValue()
        );
    }

    public ServiceResponse update(String serviceId, ServiceRequest request) {
        MonitoredService service = repository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(request.getName());
        service.setUrl(request.getUrl());
        service.setMethod(request.getMethod());
        service.setExpectedStatus(request.getExpectedStatus());
        service.setTimeoutMs(request.getTimeoutMs());
        service.setCheckIntervalSeconds(request.getCheckIntervalSeconds());
        service.setApiKeyHeader(request.getApiKeyHeader());
        service.setApiKeyValue(request.getApiKeyValue());

        return toResponse(repository.save(service));
    }
}