package com.agent.chatbot.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.chatbot.annotations.AIContext;
import com.agent.chatbot.service.AIExecution.AIInterface;
import com.agent.chatbot.service.AIExecution.enums.ExecutionTypeEnum;

@Service
public class ProductAIInterface implements AIInterface {

	@Autowired
	private ProductService productService;

	@AIContext(
		description = "Retorna uma lista de preços dos produtos vendidos na loja. Use este método apenas quando o usuário pedir diretamente informações sobre valores ou preços de produtos.",
		contexts = {ExecutionTypeEnum.MARKETPLACE_ASSISTANT}
	)
	public List<String> getPrices() {
		return this.productService.getPrices();
	}

	@AIContext(
		description = "Retorna a disponibilidade dos produtos em estoque. Use este método apenas se o usuário perguntar se um produto está disponível, em falta, ou se pode ser comprado agora.",
		contexts = {ExecutionTypeEnum.MARKETPLACE_ASSISTANT}
	)
	public List<String> getAvailability() {
		return this.productService.getAvailability();
	}
}
