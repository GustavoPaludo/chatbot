package com.agent.chatbot.service.system;

import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

	@Override
	public String getOperationTime() {
		String operationTyme = "O horário de operação é das 08:00 as 18:00 de segunda a sexta. Nos sabados, o horário é das 09:00 as 12:00.";

		return operationTyme;
	}
}
