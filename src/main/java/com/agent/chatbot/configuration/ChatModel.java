package com.agent.chatbot.configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModel {

	@Bean
	public ChatLanguageModel chatLanguageModel() {
		return OllamaChatModel.builder()
				.baseUrl("http://localhost:11434")
				.modelName("llama3")
				.build();
	}
}
