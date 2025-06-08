package com.agent.chatbot.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;

@Retention(RetentionPolicy.RUNTIME)
public @interface AIContext {
	public String description();
	public ExecutionTypeEnum[] contexts();
}
