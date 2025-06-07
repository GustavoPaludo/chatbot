package com.agent.chatbot.service.AIExecution;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.models.AIConfigContext;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

@Service
public class AIExecutionServiceImpl implements AIExecutionService {

	private final ChatLanguageModel chatLanguageModel;
	private final List<AIInterface> aiInterfaces;

	@Autowired
	public AIExecutionServiceImpl(ChatLanguageModel chatLanguageModel, List<AIInterface> aiInterfaces) {
		this.chatLanguageModel = chatLanguageModel;
		this.aiInterfaces = aiInterfaces;
	}

	@Override
	public String handleUserMessage(String userMessage, AIConfigContext aiConfigContext) throws Exception {
		if(aiConfigContext == null) {
			throw new Exception("AIContext not defined");
		}

		Map<Integer, Method> methodMap = new HashMap<>();
		Map<Integer, Object> targetMap = new HashMap<>();
		Map<Integer, String> descriptions = new HashMap<>();

		int index = 1;

		for (Object aiInterface : aiInterfaces) {
			Class<?> clazz = aiInterface.getClass();

			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(AIContext.class)) {
					AIContext annotation = method.getAnnotation(AIContext.class);
					methodMap.put(index, method);
					targetMap.put(index, aiInterface);
					descriptions.put(index, annotation.description());
					index++;
				}
			}
		}

		StringBuilder prompt = new StringBuilder();
		prompt.append("Você é um assistente que deve escolher o método que melhor responde à pergunta do usuário.\n");

		prompt.append("Você está respondendo a um usuário da cidade(instanceCode) ").append(aiConfigContext.getInstanceCode()).append(".\n");
		prompt.append("ID do usuário(userId): ").append(aiConfigContext.getUserId()).append(".\n");
		prompt.append("ID da conversa(chatId): ").append(aiConfigContext.getChatId()).append(".\n");

		prompt.append("Métodos disponíveis:\n");
		descriptions.forEach((i, desc) -> prompt.append(i).append(") ").append(desc).append("\n"));
		prompt.append("Pergunta do usuário: ").append(userMessage).append("\n");
		prompt.append(
				"Responda com o número do método seguido de ':' e os parâmetros separados por vírgula. Exemplo: 1:food\n");
		prompt.append("Se nenhum método servir, responda '0:'.\n");

		AiMessage response = chatLanguageModel.generate(List.of(UserMessage.from(prompt.toString()))).content();

		if (response == null || response.text().trim().startsWith("0:")) {
			return "Desculpe, não consigo responder sobre esse assunto";
		}

		String[] parts = response.text().split(":", 2);
		int methodNumber;
		try {
			methodNumber = Integer.parseInt(parts[0].trim());
		} catch (NumberFormatException e) {
			return "Resposta inválida da IA.";
		}

		String[] params = parts.length > 1 ? parts[1].split(",") : new String[0];

		if (!methodMap.containsKey(methodNumber)) {
			return "Desculpe, não consigo responder sobre esse assunto";
		}

		Method methodToCall = methodMap.get(methodNumber);
		Object target = targetMap.get(methodNumber);

		try {
			Object result;

			if (methodToCall.getParameterCount() == 0) {
				result = methodToCall.invoke(target);
			} else {
				Object[] args = Arrays.stream(params).map(String::trim).toArray();
				result = methodToCall.invoke(target, args);
			}

			return result == null ? "" : result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao executar o método: " + e.getMessage();
		}
	}
}
