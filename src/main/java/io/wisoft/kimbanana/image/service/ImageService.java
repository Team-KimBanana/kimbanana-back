
package io.wisoft.kimbanana.image.service;

import io.wisoft.kimbanana.image.entity.SlideImage;
import io.wisoft.kimbanana.image.repository.jdbc.JdbcImageUploadRepository;
import io.wisoft.kimbanana.image.util.ImageExpirationUtils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final JdbcImageUploadRepository imageUploadRepository;
    private final StorageService storageService;

    private static final String UPLOAD_DIR = "uploads/slide-images";
    private static final String URL_PATH_PREFIX = "/slide-images/";

    private static final String UPLOAD_DIR_THUMB = "uploads/presentation-thumbnails";
    private static final String URL_PREFIX_THUMB = "/presentation-thumbnails/";

    public String uploadImage(MultipartFile image) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = UUID.randomUUID() + ".png";
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            BufferedImage src = readImage(image);
            ImageIO.write(src, "png", filePath.toFile());
            String ImageUrl = URL_PATH_PREFIX + fileName;

            String imageId = UUID.randomUUID().toString();
            LocalDateTime uploadedAt = LocalDateTime.now();  //추후 expires_at 계산 시 사용하고자 명시적으로 넣음
            LocalDateTime expiresAt = ImageExpirationUtils.calculateExpirationDate(uploadedAt);

            SlideImage slideImage = SlideImage.builder()
                    .id(imageId)
                    .imageUrl(ImageUrl)
                    .uploadedAt(uploadedAt)
                    .expiresAt(expiresAt)
                    .build();

            imageUploadRepository.saveSlideImage(slideImage);
            return ImageUrl;
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패" + e);
        }
    }

    @Transactional
    public String uploadPresentationThumbnail(MultipartFile image, String presentationId) {
        try {
            // 디렉터리 준비
            Path thumbDir = Paths.get(UPLOAD_DIR_THUMB, presentationId);
            Files.createDirectories(thumbDir);

            storageService.clearDirectory(thumbDir);

            String fileName = UUID.randomUUID() + ".png";
            BufferedImage src = readImage(image);
            BufferedImage cropped = centerCropToAspect(src, 16, 9);
            BufferedImage resized = resizeImage(cropped, 480, 270);

            Path thumbPath = thumbDir.resolve(fileName);

            ImageIO.write(resized, "png", thumbPath.toFile());
            String responseUrl = URL_PREFIX_THUMB + presentationId + "/" + fileName;

            imageUploadRepository.savePresentationThumbnail(responseUrl, presentationId);

            return responseUrl;
        } catch (IOException e) {
            throw new RuntimeException("프레젠테이션 섬네일 업로드 실패: " + e.getMessage(), e);
        }
    }

    private BufferedImage readImage(MultipartFile image) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image.getInputStream());
            if (src == null) {
                throw new IllegalArgumentException("지원하지 않는 이미지 포맷입니다.");
            }
            return src;
        } catch (IOException e) {
            throw new RuntimeException("이미지 로딩 실패", e);
        }
    }

    private BufferedImage centerCropToAspect(final BufferedImage src, final int wRatio, final int hRatio) {
        double target = (double) wRatio / hRatio;
        int sw = src.getWidth();
        int sh = src.getHeight();
        double srcAspect = (double) sw / sh;

        int cx, cy, cw, ch;
        if (srcAspect > target) {
            // 가로가 더 넓으면 가로를 잘라냄
            ch = sh;
            cw = (int) Math.round(sh * target);
            cx = (sw - cw) / 2;
            cy = 0;
        } else {
            // 세로가 더 길면 세로를 잘라냄
            cw = sw;
            ch = (int) Math.round(sw / target);
            cx = 0;
            cy = (sh - ch) / 2;
        }
        return src.getSubimage(cx, cy, cw, ch);
    }

    private static BufferedImage resizeImage(BufferedImage src, int targetW, int targetH) {
        BufferedImage out = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(src, 0, 0, targetW, targetH, null);
        } finally {
            g2.dispose();
        }
        return out;
    }
}

