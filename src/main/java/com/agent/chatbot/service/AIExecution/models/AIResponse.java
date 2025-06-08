package com.agent.chatbot.service.AIExecution.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AIResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description; // The AI description of the operation, it holds the return / thinking of the AI itself
	private Integer methodIndex; // The index of method that will be executed (if it exists). If null, only the AIResponse will be returned.

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMethodIndex() {
		return methodIndex;
	}

	public void setMethodIndex(Integer methodIndex) {
		this.methodIndex = methodIndex;
	}
}
