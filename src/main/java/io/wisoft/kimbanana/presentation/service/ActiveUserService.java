package io.wisoft.kimbanana.presentation.service;

import io.wisoft.kimbanana.presentation.dto.request.ActiveUser;
import io.wisoft.kimbanana.presentation.dto.response.UserListMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveUserService {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, Set<ActiveUser>> activeUsers = new ConcurrentHashMap<>();
    private final Map<String, String> sessionToPresentationMap = new ConcurrentHashMap<>();

    public void addUser(String presentationId, ActiveUser user) {
        activeUsers.computeIfAbsent(presentationId, k -> ConcurrentHashMap.newKeySet()).add(user);
        sessionToPresentationMap.put(user.sessionId(), presentationId);
        broadcastUserList(presentationId);
        log.info("User added: {} to presentation: {}", user.name(), presentationId);
    }

    public void removeUser(String sessionId) {
        String presentationId = sessionToPresentationMap.remove(sessionId);
        if (presentationId != null) {
            Set<ActiveUser> users = activeUsers.get(presentationId);
            if (users != null) {
                users.removeIf(user -> user.sessionId().equals(sessionId));
                if (users.isEmpty()) {
                    activeUsers.remove(presentationId);
                } else {
                    broadcastUserList(presentationId);
                }
                log.info("User removed: sessionId={} from presentation: {}", sessionId, presentationId);
            }
        }
    }

    public List<ActiveUser> getActiveUsers(String presentationId) {
        return new ArrayList<>(activeUsers.getOrDefault(presentationId, Collections.emptySet()));
    }

    private void broadcastUserList(String presentationId) {
        List<ActiveUser> users = getActiveUsers(presentationId);
        UserListMessage message = new UserListMessage(
                presentationId,
                users,
                users.size()
        );

        String destination = "/topic/presentation." + presentationId + ".users";
        messagingTemplate.convertAndSend(destination, message);
        log.debug("Broadcasting user list to: {}, count: {}", destination, users.size());
    }
}