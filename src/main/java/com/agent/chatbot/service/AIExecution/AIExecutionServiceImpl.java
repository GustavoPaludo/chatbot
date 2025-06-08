package com.agent.chatbot.service.AIExecution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.models.AIConfigContext;
import com.agent.chatbot.service.AIExecution.models.AIResponse;
import com.agent.chatbot.service.AIExecution.prompt.PromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;

@Service
public class AIExecutionServiceImpl implements AIExecutionService {

	private static final Logger logger = LoggerFactory.getLogger(AIExecutionServiceImpl.class);

	private final ObjectMapper objectMapper;
	private final List<AIInterface> aiInterfaces;
	private final List<PromptBuilder> promptBuilders;
	private final ChatLanguageModel chatLanguageModel;

	@Autowired
	public AIExecutionServiceImpl(ChatLanguageModel chatLanguageModel, List<AIInterface> aiInterfaces, List<PromptBuilder> promptBuilders, ObjectMapper objectMapper) {
		this.aiInterfaces = aiInterfaces;
		this.promptBuilders = promptBuilders;
		this.chatLanguageModel = chatLanguageModel;
		this.objectMapper = objectMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T handleUserMessage(String userMessage, AIConfigContext context) throws Exception {
		if (context == null) {
			throw new Exception("AIContext não definido");
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

					if (Arrays.asList(annotation.contexts()).contains(context.getExecutionTypeEnum())) {
						methodMap.put(index, method);
						targetMap.put(index, aiInterface);
						descriptions.put(index, annotation.description());
						index++;
					}
				}
			}
		}

		StringBuilder prompt = new StringBuilder();
		prompt.append("Você deve interpretar a solicitação do usuário com base nos métodos disponíveis listados abaixo.\n\n");

		prompt.append("### INSTRUÇÕES CRÍTICAS:\n");
		prompt.append("- A resposta deve conter **apenas** um objeto JSON válido, **sem qualquer texto adicional antes ou depois**.\n");
		prompt.append("- Só preencha o campo `methodIndex` se um dos métodos listados abaixo puder executar **exatamente** o que o usuário solicitou, **sem interpretação subjetiva**.\n");
		prompt.append("- **Nunca** escolha um método apenas por ter relação temática. Só selecione se ele **executa exatamente** o que foi pedido.\n");
		prompt.append("- Se **nenhum método** se aplicar diretamente, você deve:\n");
		prompt.append("  - Retornar `methodIndex: null`\n");
		prompt.append("  - Preencher `description` com uma mensagem educada explicando que não é possível atender ao pedido, e sugerir temas que você pode ajudar.\n\n");

		prompt.append("### FORMATO EXATO DA RESPOSTA (sem texto fora do JSON):\n");
		prompt.append("{\n");
		prompt.append("  \"methodIndex\": número_do_método,      // ou null\n");
		prompt.append("  \"description\": \"Explicação clara ao usuário em português\"\n");
		prompt.append("}\n\n");

		prompt.append("### EXEMPLO QUANDO NENHUM MÉTODO É ADEQUADO:\n");
		prompt.append("{\n");
		prompt.append("  \"methodIndex\": null,\n");
		prompt.append("  \"description\": \"Sou a assistente da loja, mas não posso responder perguntas sobre clima. Se quiser saber sobre outro assunto que eu conheça, posso te ajudar.\"\n");
		prompt.append("}\n\n");

		prompt.append("### REGRAS RÍGIDAS:\n");
		prompt.append("- A resposta **não pode conter explicações, comentários ou texto antes ou depois do JSON**.\n");
		prompt.append("- Nunca escreva \"Here is the response\" ou similar.\n");
		prompt.append("- A resposta **obrigatoriamente é apenas o JSON representando o objeto solicitado, pois ele será descerializado e caso não esteja nesse formato ocorrerão erros**.\n");
		prompt.append("- O `methodIndex` deve ser um número inteiro válido presente na lista de métodos abaixo.\n");
		prompt.append("- **Nunca** escolha um método cujo retorno não seja o pedido. Se for perguntado de preços, não retorne horários de operação, e assim por diante.\n");
		prompt.append("- Sempre escreva em **português**.\n\n");

		prompt.append("### MÉTODOS DISPONÍVEIS:\n");
		descriptions.forEach((i, desc) -> prompt.append(i).append(") ").append(desc).append("\n"));

		String builderReturn = null;
		for (PromptBuilder builder : promptBuilders) {
			builderReturn = builder.buildPrompt(userMessage, context);
			if (builderReturn != null) {
				break;
			}
		}

		if (builderReturn == null) {
			throw new RuntimeException("Nenhum PromptBuilder retornou um prompt válido para o contexto " + context.getExecutionTypeEnum());
		}

		prompt.append(builderReturn);

		AiMessage response = chatLanguageModel.generate(List.of(UserMessage.from(prompt.toString()))).content();

		if (response == null || response.text() == null || response.text().trim().isEmpty()) {
			return (T) "Desculpe, não consegui entender a solicitação.";
		}

		String rawResponse = response.text().trim();
		AIResponse aiResponse;
		try {
			aiResponse = objectMapper.readValue(rawResponse, AIResponse.class);
		} catch (Exception e) {
			logger.error("Erro ao desserializar a resposta JSON: " + rawResponse, e);
			return (T) "Erro ao interpretar a resposta da IA.";
		}

		if (aiResponse.getMethodIndex() == null || !methodMap.containsKey(aiResponse.getMethodIndex())) {
			return (T) "Desculpe, não consigo responder sobre o assunto. Tente perguntar sobre algo relacionado ao contexto da operação.";
		}

		Method methodToCall = methodMap.get(aiResponse.getMethodIndex());
		Object target = targetMap.get(aiResponse.getMethodIndex());
		if (methodToCall == null || target == null) {
			return (T) "Método solicitado não encontrado.";
		}

		try {
			Object result;
			if (methodToCall.getParameterCount() == 0) {
				result = methodToCall.invoke(target);
			} else {
				return (T) "O método selecionado requer parâmetros e ainda não há suporte para isso.";
			}
			return (T) result;
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("Erro ao executar método '{}': {}", methodToCall.getName(), e.getMessage(), e);
			return (T) ("Erro ao executar o método: " + e.getMessage());
		}
	}
}
