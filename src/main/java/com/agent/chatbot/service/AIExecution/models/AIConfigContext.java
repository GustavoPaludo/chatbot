package com.agent.chatbot.service.AIExecution.models;

import org.jetbrains.annotations.NotNull;

import io.micrometer.common.util.StringUtils;

public class AIConfigContext {

    private final String chatId;
    private final String userId;
    private final String instanceCode;

    public AIConfigContext(@NotNull String chatId, @NotNull String userId, @NotNull String instanceCode) throws Exception {
    	if(StringUtils.isBlank(userId) || StringUtils.isBlank(userId) || StringUtils.isBlank(instanceCode)) {
    		throw new Exception("Error initializing the AI context");
    	}

        this.chatId = chatId;
        this.userId = userId;
        this.instanceCode = instanceCode;
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
}
