package io.wisoft.kimbanana.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.kimbanana.presentation.dto.response.payload.StructurePayload;
import io.wisoft.kimbanana.presentation.dto.response.SlideWrapper;
import io.wisoft.kimbanana.presentation.dto.response.payload.TitlePayload;
import io.wisoft.kimbanana.presentation.entity.Presentation;
import io.wisoft.kimbanana.presentation.dto.response.payload.SlideAddPayload;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Presentation-Controller" , description = "프레젠테이션 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/presentations")
public class PresentationRestController {

    private final PresentationService presentationService;

    @Operation(summary = "전체 슬라이드 조회", description = "프레젠테이션 ID로 전체 슬라이드 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Presentation.class)))
    @GetMapping("/{presentation-id}/slides")
    public ResponseEntity<Presentation> getSlides(@PathVariable("presentation-id") String presentationId) {
        Presentation presentation = presentationService.findByPresentationId(presentationId);

        return ResponseEntity.ok(presentation);
    }


    @Operation(summary = "단일 슬라이드 조회", description = "프레젠테이션 ID와 슬라이드 ID로 단일 슬라이드를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = SlideWrapper.class)))
    @GetMapping("/{presentation-id}/slides/{slide-id}")
    public ResponseEntity<SlideWrapper> getSlide(@PathVariable("presentation-id") String presentationId,
                                                 @PathVariable("slide-id") String slideId) {

        Slide slide = presentationService.getSlide(presentationId, slideId);
        System.out.println(slide.getSlideId());

        return ResponseEntity.ok(new SlideWrapper(slide));
    }


    @Operation(summary = "슬라이드 추가", description = "새로운 슬라이드를 프레젠테이션에 추가합니다.")
    @ApiResponse(responseCode = "200", description = "추가 성공",
            content = @Content(schema = @Schema(implementation = SlideAddPayload.class)))
    @PostMapping("/{presentation-id}/slides")
    public ResponseEntity<SlideAddPayload> createSlide(@PathVariable("presentation-id") String presentationId) {
        SlideAddPayload slide = presentationService.addSlide(presentationId);
        return ResponseEntity.ok(slide);
    }


    @Operation(summary = "슬라이드 구조 수정", description = "프레젠테이션의 슬라이드 순서/구조를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{presentation-id}/slides")
    public ResponseEntity<Integer> updateStructure(@PathVariable("presentation-id") String presentationId,
                                                   @RequestBody StructurePayload structurePayload) {

        int result = presentationService.updateStruct(presentationId, structurePayload);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "프레젠테이션 제목 수정", description = "프레젠테이션의 제목을 새로운 값으로 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "제목 수정 성공 (응답 본문 없음)"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PatchMapping("/{presentation-id}/slides/title")
    public ResponseEntity<?> updateTitle(@PathVariable("presentation-id") String presentationId,
                                         @RequestBody TitlePayload payload) {

        presentationService.updateTitle(presentationId, payload.getNewTitle());
        return ResponseEntity.noContent().build();
    }
}
