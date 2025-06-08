package com.agent.chatbot.service.AIExecution.models;

import org.jetbrains.annotations.NotNull;

import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;

import io.micrometer.common.util.StringUtils;

public class AIConfigContext {

    private final String chatId;
    private final String userId;
    private final String instanceCode;
    private final ExecutionTypeEnum executionTypeEnum;

    public AIConfigContext(@NotNull String chatId, @NotNull String userId, @NotNull String instanceCode, @NotNull ExecutionTypeEnum executionTypeEnum) throws Exception {
    	if(StringUtils.isBlank(userId) || StringUtils.isBlank(userId) || StringUtils.isBlank(instanceCode) || executionTypeEnum == null) {
    		throw new Exception("Error initializing the AI context");
    	}

        this.chatId = chatId;
        this.userId = userId;
        this.instanceCode = instanceCode;
        this.executionTypeEnum = executionTypeEnum;
    }

	public String getChatId() {
		return chatId;
	}

	public String getUserId() {
		return userId;
	}

	public String getInstanceCode() {
		return instanceCode;
	}

	public ExecutionTypeEnum getExecutionTypeEnum() {
		return executionTypeEnum;
	}
}
