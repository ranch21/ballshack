package org.ranch.ballshack.command.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.SettingsManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GPTCommand extends Command {

	public static Setting<String> api_key = new Setting<>("none", "api-key", new TypeToken<String>() {
	}.getType());
	private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
	private static final Gson gson = new Gson();
	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public GPTCommand() {
		super("gpt", "grok is this true?", "gpt set-key <api-key> | gpt <prompt>");
		SettingsManager.registerSetting(api_key);
	}

	@Override
	public void onCall(int argc, String[] argv) {
		if (argv.length <= 1) return;

		if (argv[1].equals("set-key")) {
			api_key.setValue(argv[2]);
		} else {
			if (api_key.equals("none")) {

				log("Api key is not set", true);

			} else {

				String[] prompt = Arrays.copyOfRange(argv, 1, argv.length);

				log("Prompting", true);

				getResponse(String.join(" ", prompt)).thenAccept(response -> {
					log(response, true);
				});
			}
		}
	}

	public static CompletableFuture<String> getResponse(String input) {
		return CompletableFuture.supplyAsync(() -> {
			try {
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

				URL url = new URL(API_URL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Bearer " + api_key);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);

				conn.setConnectTimeout(5000);
				conn.setReadTimeout(20000);

				try (OutputStream os = conn.getOutputStream()) {
					byte[] inputBytes = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
					os.write(inputBytes, 0, inputBytes.length);
				}

				Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8);
				String response = scanner.useDelimiter("\\A").next();
				scanner.close();

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
