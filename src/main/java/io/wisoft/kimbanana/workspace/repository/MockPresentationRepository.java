package io.wisoft.kimbanana.workspace.repository;

import io.wisoft.kimbanana.workspace.Presentation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockPresentationRepository implements PresentationRepository {
    Map<String, Presentation> store = new HashMap<>();

    public MockPresentationRepository() {
        Presentation p1 = new Presentation();
        p1.setPresentationId("p_"+ 2929);
        System.out.println(p1.getPresentationId());
        p1.setPresentationTitle("자기소개 - 신보연");
        p1.setUserId("boyeon");
        p1.setLastRevisionDate(LocalDateTime.now());

        Presentation p2 = new Presentation();
        p2.setPresentationId("p_"+ 2222);
        System.out.println(p2.getPresentationId());
        p2.setPresentationTitle("캡스톤 발표 자료");
        p2.setUserId("yenniii");
        p2.setLastRevisionDate(LocalDateTime.now());

        store.put(p1.getPresentationId(), p1);
        store.put(p2.getPresentationId(), p2);
    }
    
    @Override
    public String add(final String userId) {
        Presentation p = new Presentation();
        p.setPresentationId("p_"+ 1111);
        p.setUserId(userId);
        p.setPresentationTitle("Untitled");
        p.setLastRevisionDate(LocalDateTime.now());
        store.put(p.getPresentationId(), p);
        return p.getPresentationId();
    }

    @Override
    public List<Presentation> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public int delete(final String presentationId) {
        store.remove(String.valueOf(presentationId));
        return 0;
    }
}
