//package io.wisoft.kimbanana.workspace.repository.mock;
//
//import io.wisoft.kimbanana.workspace.Workspace;
//import io.wisoft.kimbanana.workspace.repository.WorkspaceRepository;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MockWorkspaceRepository implements WorkspaceRepository {
//    Map<String, Workspace> store = new HashMap<>();
//
//    public MockWorkspaceRepository() {
//        Workspace p1 = new Workspace();
//        p1.setPresentationId("p_"+ 2929);
//        System.out.println(p1.getPresentationId());
//        p1.setPresentationTitle("자기소개 - 신보연");
//        p1.setUserId("boyeon");
//        p1.setLastRevisionDate(LocalDateTime.now());
//
//        Workspace p2 = new Workspace();
//        p2.setPresentationId("p_"+ 2222);
//        System.out.println(p2.getPresentationId());
//        p2.setPresentationTitle("캡스톤 발표 자료");
//        p2.setUserId("yenniii");
//        p2.setLastRevisionDate(LocalDateTime.now());
//
//        store.put(p1.getPresentationId(), p1);
//        store.put(p2.getPresentationId(), p2);
//    }
//
//    @Override
//    public String add(final String userId) {
//        Workspace p = new Workspace();
//        p.setPresentationId("p_"+ 1111);
//        p.setUserId(userId);
//        p.setPresentationTitle("Untitled");
//        p.setLastRevisionDate(LocalDateTime.now());
//        store.put(p.getPresentationId(), p);
//        return p.getPresentationId();
//    }
//
//    @Override
//    public List<Workspace> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    @Override
//    public int delete(final String presentationId) {
//        store.remove(String.valueOf(presentationId));
//        return 0;
//    }
//}