package com.spring.qldapmbe.manageCourse.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.qldapmbe.manageCourse.demo.entity.ChatMessage;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.service.UserService;


@Controller
public class ChatController {
	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	private UserService userService;

	public ChatController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	

	@GetMapping("/chatting")
	public String getFormChatting(Model model) {
		User user = userService.getCurrentLoginUser();
		model.addAttribute("currentUser", user);
		return "index";
	}

	@MessageMapping("/chat.sendMessage")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		String destination = "/course-chatting/" + chatMessage.getRoomId();
		messagingTemplate.convertAndSend(destination, chatMessage);
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	public ChatMessage addUser(@Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
		String destination = "/course-chatting/" + chatMessage.getRoomId();

		simpMessageHeaderAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		simpMessageHeaderAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

		messagingTemplate.convertAndSend(destination, chatMessage);

		return chatMessage;
	}
}
