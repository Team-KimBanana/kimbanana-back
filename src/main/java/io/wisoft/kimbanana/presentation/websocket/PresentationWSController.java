package io.wisoft.kimbanana.presentation.websocket;

import io.wisoft.kimbanana.presentation.dto.request.ActiveUser;
import io.wisoft.kimbanana.presentation.entity.Slide;
import io.wisoft.kimbanana.presentation.service.ActiveUserService;
import io.wisoft.kimbanana.presentation.service.PresentationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PresentationWSController {
    private final PresentationService presentationService;
    private final ActiveUserService activeUserService;

    @MessageMapping("/slide.edit.presentation.{presentationId}.slide.{currentSlideId}")
    public void editSlide(@DestinationVariable String presentationId,
                          @DestinationVariable String currentSlideId,
                          @Payload Slide slide) {

        log.info("수정 요청 슬라이드: {}", slide.getSlideId());

        presentationService.updateSlide(presentationId, currentSlideId, slide);
    }


    @MessageMapping("/presentation.{presentationId}.join")
    public void joinPresentation(@DestinationVariable String presentationId, SimpMessageHeaderAccessor headerAccessor) {

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if(sessionAttributes == null) {
            log.warn("세션 속성 정보를 찾을 수 없음 {}", presentationId);
            return;
        }

        String userId = (String) sessionAttributes.get("userId");
        String userName = (String) sessionAttributes.get("userName");
        String userType =  (String) sessionAttributes.get("userType");
        String sessionId = headerAccessor.getSessionId();

        log.info("참여한 userId: {}, userNameU: {}, userType: {}, sessionId: {}", userId , userName, userType, sessionId);

        ActiveUser user = new ActiveUser(
                userId,
                userName,
                ActiveUser.UserType.valueOf(userType),
                sessionId
        );

        activeUserService.addUser(presentationId, user);
        log.info("User {} 참여했습니다. presenation {}", userId, presentationId);
    }
}



