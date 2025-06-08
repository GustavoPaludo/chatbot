package com.agent.chatbot.service.AIExecution.prompt;

import org.springframework.stereotype.Service;

import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;
import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

@Service
public class ChatBotPromptBuilder implements PromptBuilder {

	@Override
	public String buildPrompt(String userMessage, AIConfigContext context) {
		if(!ExecutionTypeEnum.CHATBOT_ASSISTANT.equals(context.getExecutionTypeEnum())) {
			return null;
		}

		StringBuilder prompt = new StringBuilder();
		prompt.append("Você é um chatbot que deve iteragir com o usuário e responder suas dúvidas.\n");

		prompt.append("Você está respondendo a um usuário da cidade(instanceCode) ").append(context.getInstanceCode()).append(".\n");
		prompt.append("ID do usuário(userId): ").append(context.getUserId()).append(".\n");
		prompt.append("ID da conversa(chatId): ").append(context.getChatId()).append(".\n");

		prompt.append("Pergunta do usuário: ").append(userMessage).append("\n");

		return prompt.toString();
	}
}
