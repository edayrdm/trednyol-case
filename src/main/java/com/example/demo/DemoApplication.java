package com.example.demo;

import com.example.demo.cart.CartDisplay;
import com.example.demo.cart.CartItem;
import com.example.demo.cart.CartService;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.ItemService;
import com.example.demo.items.VasItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

		String inputFilePath = "src/input.json";
		String outputFilePath = "src/output.json";

		ObjectMapper objectMapper = new ObjectMapper();
		CartService cartService = new CartService(new ItemService());

		try {
			JsonNode inputCommands = objectMapper.readTree(new File(inputFilePath));
			List<JsonNode> results = new ArrayList<>();

			inputCommands.forEach(commandNode -> {

				String command = commandNode.get("command").asText();
				JsonNode payload = commandNode.get("payload");

				ObjectNode resultNode = objectMapper.createObjectNode();
				switch (command) {
					case "addItem":
						handleAddItemCommand(cartService, payload, resultNode);
						break;
					case "addVasItemToItem":
						handleAddVasItemToItemCommand(cartService, payload, resultNode);
						break;
					case "removeItem":
						handleRemoveItemCommand(cartService, payload, resultNode);
						break;
					case "resetCart":
						handleResetCartCommand(cartService, resultNode);
						break;
					case "displayCart":
						handleDisplayCartCommand(cartService, resultNode);
						break;
					default:
						resultNode.put("result", false);
						resultNode.put("message", "Unknown command");
				}
				results.add(resultNode);
			});

			objectMapper.writeValue(new File(outputFilePath), results);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleAddItemCommand(CartService cartService, JsonNode payload, ObjectNode resultNode) {

		int itemId = payload.get("itemId").asInt();
		int categoryId = payload.get("categoryId").asInt();
		int sellerId = payload.get("sellerId").asInt();
		double price = payload.get("price").asDouble();
		int quantity = payload.get("quantity").asInt();

		ResponseEntity result = cartService.addItem(itemId, categoryId, sellerId, price, quantity);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleAddVasItemToItemCommand(CartService cartService, JsonNode payload, ObjectNode resultNode) {

		int itemId = payload.get("itemId").asInt();
		int vasItemId = payload.get("vasItemId").asInt();
		int vasCategoryId = payload.get("vasCategoryId").asInt();
		int vasSellerId = payload.get("vasSellerId").asInt();
		double price = payload.get("price").asDouble();
		int quantity = payload.get("quantity").asInt();

		ResponseEntity result = cartService.addVasItemToItem(itemId, vasItemId, vasCategoryId, vasSellerId, price, quantity);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleRemoveItemCommand(CartService cartService, JsonNode payload, ObjectNode resultNode) {

		int itemId = payload.get("itemId").asInt();
		ResponseEntity result = cartService.removeItem(itemId);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleResetCartCommand(CartService cartService, ObjectNode resultNode) {


		cartService.resetCart();
		resultNode.put("result", true);
		resultNode.put("message", "Cart reset successfully.");
	}

	private static void handleDisplayCartCommand(CartService cartService, ObjectNode resultNode) {

		CartDisplay cartDisplay = cartService.displayCart();
		ObjectNode messageNode = resultNode.putObject("message");

			messageNode.put("totalAmount", cartDisplay.getTotalAmount().value());
			messageNode.put("totalDiscount", cartDisplay.getTotalDiscount().value());
			messageNode.put("appliedPromotionId", cartDisplay.getAppliedPromotionId());

			ObjectMapper objectMapper = new ObjectMapper();
			List<ObjectNode> itemNodes = new ArrayList<>();
			for (CartItem cartItem : cartDisplay.getItems()) {
				ObjectNode itemNode = objectMapper.createObjectNode();
				itemNode.put("itemId", cartItem.getItem().getID());
				itemNode.put("categoryId", cartItem.getItem().getCategoryID());
				itemNode.put("sellerId", cartItem.getItem().getSellerID());
				itemNode.put("price", cartItem.getItem().getPrice().value());
				itemNode.put("quantity", cartItem.getQuantity());

				List<ObjectNode> vasItemNodes = new ArrayList<>();
				for (VasItem vasItem : cartItem.getVasItemList()) {
					ObjectNode vasItemNode = objectMapper.createObjectNode();
					vasItemNode.put("vasItemId", vasItem.getID());
					vasItemNode.put("vasCategoryId", vasItem.getCategoryID());
					vasItemNode.put("vasSellerId", vasItem.getSellerID());
					vasItemNode.put("price", vasItem.getPrice().value());
					vasItemNode.put("quantity", vasItem.getQuantity());
					vasItemNodes.add(vasItemNode);
				}
				itemNode.putArray("vasItems").addAll(vasItemNodes);
				itemNodes.add(itemNode);
			}
			messageNode.putArray("items").addAll(itemNodes);
		}
}
