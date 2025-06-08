package com.agent.chatbot.service.AIExecution;

import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

public interface AIExecutionService {
	public <T> T handleUserMessage(String userMessage, AIConfigContext aiConfigContext) throws Exception;
}
