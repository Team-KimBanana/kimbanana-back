package io.wisoft.kimbanana.workspace.service;

import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
import java.util.List;

public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<Workspace> findAllPresentation(String userId) {
        return workspaceRepository.findAllPresentation(userId);
    }

    public Workspace findPresentation(final String presentationId) {
        return workspaceRepository.findPresentation(presentationId);
    }

    public String addPresentation(final String userId) {
        return workspaceRepository.add(userId);
    }

    public void deletePresentation(final String presentationId) {
        workspaceRepository.delete(presentationId);
    }
}