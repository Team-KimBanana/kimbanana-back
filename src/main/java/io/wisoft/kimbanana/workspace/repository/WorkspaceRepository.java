package io.wisoft.kimbanana.workspace.repository;

import io.wisoft.kimbanana.workspace.Workspace;
import java.util.List;

public interface WorkspaceRepository {
    String add(final String userId);

    List<Workspace> findAll();

    int delete(final String presentationId);
}