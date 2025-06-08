package com.agent.chatbot.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.AIInterface;
import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;

@Service
public class SystemAIInterface implements AIInterface {

	@Autowired
	private SystemService systemService;

	@AIContext(
		description = "Retorna os horários de funcionamento da loja, incluindo abertura e fechamento. Use este método somente se o usuário pedir informações sobre horário de atendimento ou funcionamento.",
		contexts = {ExecutionTypeEnum.MARKETPLACE_ASSISTANT, ExecutionTypeEnum.CHATBOT_ASSISTANT}
	)
	public String getOperationTime() {
		return this.systemService.getOperationTime();
	}
}
