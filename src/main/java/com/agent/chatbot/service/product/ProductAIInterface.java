package com.agent.chatbot.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.AIInterface;

@Service
public class ProductAIInterface implements AIInterface {

	@Autowired
	private ProductService productService;

	@AIContext(description = "Retorna a lista de preços dos produtos")
	public List<String> getPrices() {
		return this.productService.getPrices();
	}

	@AIContext(description = "Retorna a disponibilidade dos produtos")
	public List<String> getAvailability() {
		return this.productService.getAvailability();
	}
}
