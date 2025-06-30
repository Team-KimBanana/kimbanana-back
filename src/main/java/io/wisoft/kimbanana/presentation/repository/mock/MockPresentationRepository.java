//package io.wisoft.kimbanana.presentation.repository.mock;
//
//import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
//import io.wisoft.kimbanana.presentation.entity.Presentation;
//import io.wisoft.kimbanana.presentation.entity.Slide;
//import io.wisoft.kimbanana.presentation.dto.response.SlideAddResponse;
//import io.wisoft.kimbanana.presentation.repository.PresentationRepository;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MockPresentationRepository implements PresentationRepository {
//
//    private static final Map<String, Presentation> store = new HashMap<>();
//
//    public MockPresentationRepository() {
//        Presentation p1 = new Presentation();
//        p1.setPresentationId("p_" + 2929);
//        System.out.println(p1.getPresentationId());
//        p1.setPresentationTitle("자기소개 - 신보연");
//        p1.setUserId("boyeon");
//        p1.setLastRevisionDate(LocalDateTime.now());
//
//        // 슬라이드 추가
//        Slide s1 = new Slide();
//        s1.setSlideId("s_" + UUID.randomUUID());
//        s1.setSlideOrder(1);
//        s1.setLastRevisionDate(LocalDateTime.now());
//        s1.setLastRevisionUserId("boyeon");
//        s1.setData("첫 번째 슬라이드");
//
//        Slide s2 = new Slide();
//        s2.setSlideId("s_" + UUID.randomUUID());
//        s2.setSlideOrder(2);
//        s2.setLastRevisionDate(LocalDateTime.now());
//        s2.setLastRevisionUserId("boyeon");
//        s2.setData("두 번째 슬라이드");
//
//        List<Slide> slideList = new ArrayList<>();
//        slideList.add(s1);
//        slideList.add(s2);
//
//        p1.setSlides(slideList);
//
//        // store 에 추가
//        store.put(p1.getPresentationId(), p1);
//        System.out.println(store);
//    }
//
//    @Override
//    public Slide findByIdSlide(final String presentationId, final String slideId) {
//        Presentation presentation = store.get(presentationId);
//        List<Slide> sildes = presentation.getSlides();
//        for (Slide slide : sildes) {
//            if (slideId.equals(slide.getSlideId())) {
//                return slide;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void updateSlide(final String presentationId, final String slideId, final Slide updatedSlide) {
//        Presentation presentation = store.get(presentationId);
//        if (presentation == null || presentation.getSlides() == null) {
//            return;
//        }
//
//        for (int i = 0; i < presentation.getSlides().size(); i++) {
//            Slide s = presentation.getSlides().get(i);
//            if (s.getSlideId().equals(slideId)) {
//                updatedSlide.setSlideId(slideId); // 슬라이드 ID는 유지해야 함
//                presentation.getSlides().set(i, updatedSlide);
//                updatedSlide.setLastRevisionDate(LocalDateTime.now());
//                return;
//            }
//        }
//    }
//
//    @Override
//    public void updateStruct(final String presentationId, final List<SlideStructure> slideStructures) {
//        Presentation presentation = store.get(presentationId);
//        //todo: 구조 업데이트 구현 예정
//    }
//
//    public SlideAddResponse createSlide(String presentationId) {
//        SlideAddResponse response = new SlideAddResponse();
//        Presentation presentation = findByPresentationId(presentationId);
//
//        List<Slide> slideList = presentation.getSlides();
//        int order = slideList.size(); // 현재 슬라이드 수 → 다음 슬라이드 순서;
//
//        //슬라이드 생성
//        Slide slide = new Slide();
//        String slideId = "s_" + UUID.randomUUID();
//
//        slide.setSlideId(slideId);
//        int orderNum = order + 1;
//        slide.setSlideOrder(orderNum);
//        slide.setLastRevisionDate(LocalDateTime.now());
//
//        // 리스트에 새 슬라이드 추가
//        slideList.add(slide);
//
//        // 저장소에 업데이트된 presentation 다시 저장
//        store.put(presentationId, presentation);
//
//        //리턴값
//        response.setOrder(orderNum);
//
//        //todo-나중에 저장된 prestnation 에서 찾는거 추가하기
//        response.setSlideId(slideId);
//
//        return response;
//    }
//
//
//    public Presentation findByPresentationId(String presentationId) {
//        if (store.get(presentationId) == null) {
//            Presentation newPresentation = new Presentation();
//            newPresentation.setPresentationId(presentationId);
//
//            List<Slide> slide = new ArrayList<>();
//            Slide slide1 = new Slide();
//            slide1.setSlideId("s_" + 2292);
//
////            newPresentation.setSlides(slide);
//            slide.add(slide1);
//
//            store.put(presentationId, newPresentation); // 데이터가 없어서 임시로 생성해줌
//        }
//
//        Presentation presentation = store.get(presentationId);
//        return presentation;
//    }
//
//    public int deleteSlide(String presentationId, String slideId) {
//        Presentation presentation = store.get(presentationId);
//
//        if (presentation == null) {
//            return 0;
//        }
//
//        List<Slide> slides = presentation.getSlides();
//        if (slides == null) {
//            return 0;
//        }
//
//        for (int i = 0; i < slides.size(); i++) {
//            if (slides.get(i).getSlideId().equals(slideId)) {
//                slides.remove(i);
//                return 1;
//            }
//        }
//
//        Presentation presentation1 = findByPresentationId(presentationId);
//        System.out.println("here " + presentation1.getSlides().size());
//
//        return 0;
//    }
//
//}
