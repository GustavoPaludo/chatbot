package com.agent.chatbot.service.AIExecution.prompt;

import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

public interface PromptBuilder {
	public String buildPrompt(String userMessage, AIConfigContext context);
}
