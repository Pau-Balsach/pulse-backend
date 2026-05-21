package com.pulse.project.service;

import com.pulse.project.dto.ProjectRequest;
import com.pulse.project.dto.ProjectResponse;
import com.pulse.project.entity.Project;
import com.pulse.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse create(ProjectRequest request, String userId) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .userId(userId)
                .build();

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public List<ProjectResponse> getAll(String userId) {
        return projectRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt()
        );
    }
}