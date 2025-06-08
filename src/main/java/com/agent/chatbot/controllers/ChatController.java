package com.agent.chatbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.agent.chatbot.service.AIExecution.AIExecutionService;
import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;
import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private AIExecutionService aiExecutionService;

	@PostMapping
	public <T> T chat(@RequestBody String message, 
			@RequestParam(required = true) String chatId,
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String instanceCode,
			@RequestParam(required = true) ExecutionTypeEnum executionTypeEnum) throws Exception {

		AIConfigContext aiContext = new AIConfigContext(chatId, userId, instanceCode, executionTypeEnum);

		return aiExecutionService.handleUserMessage(message, aiContext);
	}
}
