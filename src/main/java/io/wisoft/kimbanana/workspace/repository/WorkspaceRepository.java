package io.wisoft.kimbanana.workspace.repository;

import java.util.List;
import io.wisoft.kimbanana.workspace.Workspace;

public interface WorkspaceRepository {
    String add(final String userId);

    List<Workspace> findAll();

    int delete(final String presentationId);
}
