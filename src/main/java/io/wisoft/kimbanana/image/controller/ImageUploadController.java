
package io.wisoft.kimbanana.image.controller;

import io.wisoft.kimbanana.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image) {
        String imageUrl = imageService.uploadImage(image);
        return ResponseEntity.ok(imageUrl);
    }
}
