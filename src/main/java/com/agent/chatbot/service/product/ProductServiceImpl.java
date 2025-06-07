package com.agent.chatbot.service.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public List<String> getPrices() {
		List<String> productPrice = new ArrayList<>(Arrays.asList("Produto 1: R$ 10", "Produto 2: R$ 5", "Produto 3: R$ 1"));

		return productPrice;
	}

	@Override
	public List<String> getAvailability() {
		List<String> productAvailability = new ArrayList<>(Arrays.asList("Produto 1: 10 unidades", "Produto 2: 5 unidades", "Produto 3: 1 unidade"));

		return productAvailability;
	}
}
