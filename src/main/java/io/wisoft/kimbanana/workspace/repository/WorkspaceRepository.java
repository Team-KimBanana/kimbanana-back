package io.wisoft.kimbanana.workspace.repository;

import io.wisoft.kimbanana.workspace.Workspace;
import java.util.List;

public interface WorkspaceRepository {
    List<Workspace> findAllPresentation(String userId);

    String add(final String userId);

    int delete(final String presentationId);

    Workspace findPresentation(final String presentationId);
}