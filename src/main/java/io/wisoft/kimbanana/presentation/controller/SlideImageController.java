//package io.wisoft.kimbanana.presentation.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//public class SlideImageController {
//
//    private final SlideImageService slideImageService;
//
//    @PostMapping("/upload-image")
//    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
//        String imageUrl = slideImageService.uploadImage(image);
//        return ResponseEntity.ok(imageUrl);
//    }
//}