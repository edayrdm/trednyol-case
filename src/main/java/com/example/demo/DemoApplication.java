package com.example.demo;

import com.example.demo.cart.Cart;
import com.example.demo.cart.CartItem;
import com.example.demo.common.Money;
import com.example.demo.common.PromotionDetails;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.DefaultItem;
import com.example.demo.items.DigitalItem;
import com.example.demo.items.Item;
import com.example.demo.items.VasItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

		String inputFilePath = "src/input.json";
		String outputFilePath = "src/output.json";

		ObjectMapper objectMapper = new ObjectMapper();
		Cart cart = new Cart();

		try {
			JsonNode inputCommands = objectMapper.readTree(new File(inputFilePath));
			List<JsonNode> results = new ArrayList<>();

			inputCommands.forEach(commandNode -> {

				String command = commandNode.get("command").asText();
				JsonNode payload = commandNode.get("payload");

				ObjectNode resultNode = objectMapper.createObjectNode();
				switch (command) {
					case "addItem":
						handleAddItemCommand(cart, payload, resultNode);
						break;
					case "addVasItemToItem":
						handleAddVasItemToItemCommand(cart, payload, resultNode);
						break;
					case "removeItem":
						handleRemoveItemCommand(cart, payload, resultNode);
						break;
					case "resetCart":
						handleResetCartCommand(cart, resultNode);
						break;
					case "displayCart":
						handleDisplayCartCommand(cart, resultNode);
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

	private static void handleAddItemCommand(Cart cart, JsonNode payload, ObjectNode resultNode) {

		int itemId = payload.get("itemId").asInt();
		int categoryId = payload.get("categoryId").asInt();
		int sellerId = payload.get("sellerId").asInt();
		double price = payload.get("price").asDouble();
		int quantity = payload.get("quantity").asInt();

		//we have 2 types of item digitalItem or defaultItem
		Item item;
		if(categoryId == 7889)
			item = new DigitalItem(itemId, sellerId, categoryId,new Money((price)));
		else
			item = new DefaultItem(itemId, sellerId, categoryId,new Money((price)));

		ResponseEntity result = cart.addItemToList(item, quantity);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleAddVasItemToItemCommand(Cart cart, JsonNode payload, ObjectNode resultNode) {

		int itemId = payload.get("itemId").asInt();
		int vasItemId = payload.get("vasItemId").asInt();
		int vasCategoryId = payload.get("vasCategoryId").asInt();
		int vasSellerId = payload.get("vasSellerId").asInt();
		double price = payload.get("price").asDouble();
		int quantity = payload.get("quantity").asInt();

		VasItem vasItem = new VasItem(vasItemId, vasSellerId, vasCategoryId, new Money((price)), quantity);
		ResponseEntity result = cart.addVasItemToItem(itemId, vasItem);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleRemoveItemCommand(Cart cart, JsonNode payload, ObjectNode resultNode) {
		int itemId = payload.get("itemId").asInt();
		ResponseEntity result = cart.removeItem(itemId);

		resultNode.put("result", result.isResult());
		resultNode.put("message", result.getMessage());
	}

	private static void handleResetCartCommand(Cart cart, ObjectNode resultNode) {
		cart.resetItems();

		resultNode.put("result", true);
		resultNode.put("message", "Cart reset successfully.");
	}

	private static void handleDisplayCartCommand(Cart cart, ObjectNode resultNode) {

		resultNode.put("result", true);
		ObjectNode messageNode = resultNode.putObject("message");

		// checking for applied promotion details
		PromotionDetails details = cart.getTotalDiscount();
		Money totalPrice = cart.getTotalPrice();
		Money totalDiscount = details.getDiscountAmount();
		Money totalAmount = totalPrice.sub(totalDiscount);


		messageNode.put("totalAmount", totalAmount.value());
		messageNode.put("totalDiscount", totalDiscount.value());
		messageNode.put("appliedPromotionId", details.getPromotionId());

		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> itemNodes = new ArrayList<>();
		for (CartItem cartItem : cart.getCartItemList()) {
			ObjectNode itemNode = objectMapper.createObjectNode();
			itemNode.put("itemId", cartItem.getItem().getID());
			itemNode.put("categoryId", cartItem.getItem().getCategoryID());
			itemNode.put("sellerId", cartItem.getItem().getSellerID());
			itemNode.put("price", cartItem.getTotalPrice().value());
			itemNode.put("quantity", cartItem.getQuantity());

			List<ObjectNode> vasItemNodes = new ArrayList<>();
			for (VasItem vasItem : cartItem.getVasItemList()) {
				ObjectNode vasItemNode =objectMapper.createObjectNode();
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
