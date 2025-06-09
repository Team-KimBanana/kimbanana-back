package io.wisoft.kimbanana.workspace.service;

import io.wisoft.kimbanana.workspace.Presentation;
import io.wisoft.kimbanana.workspace.repository.MockPresentationRepository;
import io.wisoft.kimbanana.workspace.repository.PresentationRepository;
import java.util.List;

public class PresentationService {

    private final MockPresentationRepository presentationRepository;
    public PresentationService(MockPresentationRepository presentationService) {
        this.presentationRepository = presentationService;
    }

    public String addPresentation(final String userId) {
        return presentationRepository.add(userId);
    }

    public List<Presentation> findPresentation() {
        return presentationRepository.findAll();
    }

    public int deletePresentation(final String presentationId) {
        return presentationRepository.delete(presentationId);
    }

}
