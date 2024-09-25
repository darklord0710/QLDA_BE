package com.spring.qldapmbe.manageCourse.demo.config;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.spring.qldapmbe.manageCourse.demo.entity.ChatMessage;
import com.spring.qldapmbe.manageCourse.demo.entity.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

	private final SimpMessageSendingOperations messageTemplate;

	@EventListener
	public void handleWebSockerDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor
				.wrap(sessionDisconnectEvent.getMessage());
		String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
		String roomId = (String) stompHeaderAccessor.getSessionAttributes().get("roomId");

		if (username != null) {
			log.info("User disconnected : {}", username);
			var chatMessage = ChatMessage.builder().sender(username).roomId(roomId)
					.type(MessageType.LEAVE)
					.build();
			messageTemplate.convertAndSend("/course-chatting/" + chatMessage.getRoomId(),
					chatMessage);
		}

	}

}
