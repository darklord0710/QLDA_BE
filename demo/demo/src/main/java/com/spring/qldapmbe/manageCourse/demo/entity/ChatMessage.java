package com.spring.qldapmbe.manageCourse.demo.entity;

import java.awt.TrayIcon.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
	private String content;
	private String sender;
	private MessageType type;
	private String roomId;
}
