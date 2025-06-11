package io.wisoft.kimbanana.presentation.repository;

import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.HashMap;
import java.util.Map;

public class MockSlideRepository implements SlideRepository {

    private static final Map<String, Slide> store = new HashMap<>();
    private static long sequence = 0L;

//
//    public String addSlide() {
//        String slideId = String.valueOf(sequence++);
//        String key = "s_" + slideId;
//        System.out.println(key);
//        store.put(slideId, new Slide());
//        return key;
//    }

    public Slide findById(String slideId) {
        store.put(slideId, new Slide());
        return store.get(slideId);
    }

    public void update(String slideId, Slide updateParam) {
        Slide findSlide = findById(slideId);
        findSlide.setLastRevisionDate(updateParam.getLastRevisionDate());
        findSlide.setLastRevisionUserId(updateParam.getLastRevisionUserId());
        findSlide.setSlideOrder(updateParam.getSlideOrder());
        findSlide.setData(updateParam.getData());
    }

    public void delete(String slideId) {
        store.remove(slideId);
    }

    public void clearStore() {
        store.clear();
    }
}
