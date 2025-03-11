package org.ranch.ballshack.command.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.setting.SettingSaver;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GPTCommand extends Command {

	public static String api_key = "none";
	private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
	private static final Gson gson = new Gson();
	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public GPTCommand() {
		super("gpt");
	}

	@Override
	public void onCall(String[] args) {
		if (args.length <= 1) return;

		if (args[1].equals("set-key")) {
			api_key = args[2];
			SettingSaver.SCHEDULE_SAVE_MODULES.set(true);
		} else {
			if (api_key.equals("none")) {

				mc.player.sendMessage(Text.literal("Api key is not set"), false);

			} else {

				String[] prompt = Arrays.copyOfRange(args, 1, args.length);

				getResponse(String.join(" ", prompt)).thenAccept(response -> {
					mc.player.sendMessage(Text.literal("BallsGPT: " + response), false);
				});

			}
		}
	}

	public static CompletableFuture<String> getResponse(String input) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				// Build JSON payload
				JsonObject requestBody = new JsonObject();
				requestBody.addProperty("model", "deepseek/deepseek-chat:free");
				requestBody.addProperty("max_tokens", 200);

				JsonArray messages = new JsonArray();
				JsonObject systemMessage = new JsonObject();
				systemMessage.addProperty("role", "system");
				systemMessage.addProperty("content", "You are a helpful assistant inside the video game Minecraft, DO NOT use markdown formatting, only simple plain text, keep your answers straight to the point.");
				messages.add(systemMessage);

				JsonObject userMessage = new JsonObject();
				userMessage.addProperty("role", "user");
				userMessage.addProperty("content", input);
				messages.add(userMessage);
				requestBody.add("messages", messages);

				// Create HTTP connection
				URL url = new URL(API_URL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Bearer " + api_key);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);

				// Set timeouts to prevent freezing
				conn.setConnectTimeout(5000); // 5 seconds to establish connection
				conn.setReadTimeout(20000); // 10 seconds to read response

				// Send JSON request
				try (OutputStream os = conn.getOutputStream()) {
					byte[] inputBytes = gson.toJson(requestBody).getBytes("utf-8");
					os.write(inputBytes, 0, inputBytes.length);
				}

				// Read response
				Scanner scanner = new Scanner(conn.getInputStream(), "utf-8");
				String response = scanner.useDelimiter("\\A").next();
				scanner.close();

				// Parse and return response
				JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
				return jsonResponse.getAsJsonArray("choices")
						.get(0).getAsJsonObject()
						.getAsJsonObject("message")
						.get("content").getAsString();

			} catch (Exception e) {
				return "Error: " + e.getMessage();
			}
		}, executor);
	}
}
