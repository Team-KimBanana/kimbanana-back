
package io.wisoft.kimbanana.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.kimbanana.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image-Controller" , description = "이미지 처리 관련 API")
@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    @Operation(summary = "이미지 url 생성하기")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image) {
        String imageUrl = imageService.uploadImage(image);
        return ResponseEntity.ok(imageUrl);
    }


    @Operation(summary = "썸네일 이미지 url 생성하기")
    @PostMapping("/thumbnails/presentation")
    public ResponseEntity<String> uploadThumbnails(@RequestParam("file") MultipartFile image,
                                                   @RequestParam("presentationId") String presentationId) {

        String response = imageService.uploadPresentationThumbnail(image, presentationId);
        return ResponseEntity.ok(response);
    }
}
