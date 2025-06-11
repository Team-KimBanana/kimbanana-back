package io.wisoft.kimbanana.presentation.util;

import io.wisoft.kimbanana.presentation.dto.response.PresentationStructureResponse.SlideStructure;
import io.wisoft.kimbanana.presentation.entity.Slide;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StructureConvert {

    public static List<SlideStructure> structureList(List<Slide> slides) {
        List<SlideStructure> slideStructures = slides.stream()
                .sorted(Comparator.comparingInt(slide -> slide.getSlideOrder()))
                .map(slide -> {
                    SlideStructure s = new SlideStructure();
                    s.setSlideId(slide.getSlideId());
                    s.setOrder(slide.getSlideOrder());
                    return s;
                })
                .collect(Collectors.toList());
        return slideStructures;
    }
}
