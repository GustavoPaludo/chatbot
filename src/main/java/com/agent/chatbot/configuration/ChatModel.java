package com.agent.chatbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

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
