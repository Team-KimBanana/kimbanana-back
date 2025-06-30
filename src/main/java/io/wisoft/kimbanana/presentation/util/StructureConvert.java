package io.wisoft.kimbanana.presentation.util;

import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StructureConvert {

    //재정렬 수행하는 로직
    public static List<Slide> sortSlides(List<Slide> slides) {
        return slides.stream()
                .sorted(Comparator.comparingInt(Slide::getSlideOrder))
                .collect(Collectors.toList());
    }


    //응답 구조 변환
    public static List<SlideStructure> structureList(List<Slide> slides) {
        return sortSlides(slides).stream()
                .map(slide -> SlideStructure.builder()
                        .slideId(slide.getSlideId())
                        .order(slide.getSlideOrder())
                        .build())
           .collect(Collectors.toList());


    }
}