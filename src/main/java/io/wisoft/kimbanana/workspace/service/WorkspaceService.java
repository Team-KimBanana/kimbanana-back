package io.wisoft.kimbanana.workspace.service;

import io.wisoft.kimbanana.workspace.Workspace;
import io.wisoft.kimbanana.workspace.repository.MockWorkspaceRepository;
import java.util.List;

public class WorkspaceService {

    private final MockWorkspaceRepository presentationRepository;
    public WorkspaceService(MockWorkspaceRepository presentationService) {
        this.presentationRepository = presentationService;
    }

    public String addPresentation(final String userId) {
        return presentationRepository.add(userId);
    }

    public List<Workspace> findPresentation() {
        return presentationRepository.findAll();
    }

    public int deletePresentation(final String presentationId) {
        return presentationRepository.delete(presentationId);
    }

}
