package com.agent.chatbot.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.AIInterface;

@Service
public class SystemAIInterface implements AIInterface {

	@Autowired
	private SystemService systemService;

	@AIContext(description = "Retorna o horário de operação")
	public String getOperationTime() {
		return this.systemService.getOperationTime();
	}
}
