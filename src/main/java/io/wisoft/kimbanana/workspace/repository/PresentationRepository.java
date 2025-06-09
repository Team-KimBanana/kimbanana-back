package io.wisoft.kimbanana.workspace.repository;

import java.util.List;
import io.wisoft.kimbanana.workspace.Presentation;

public interface PresentationRepository {
    String add(final String userId);

    List<Presentation> findAll();

    int delete(final String presentationId);
}
