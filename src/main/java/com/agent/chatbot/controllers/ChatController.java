package com.agent.chatbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.agent.chatbot.service.AIExecution.AIExecutionService;
import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private AIExecutionService aiExecutionService;

	@PostMapping
	public String chat(@RequestBody String message, 
			@RequestParam(required = true) String chatId,
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String instanceCode) throws Exception {

		AIConfigContext aiContext = new AIConfigContext(chatId, userId, instanceCode);

		return aiExecutionService.handleUserMessage(message, aiContext);
	}
}
