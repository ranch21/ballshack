package org.ranch.ballshack.command.suggestors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.setting.ClientSettingSaver;

import java.util.concurrent.CompletableFuture;

public class SettingSuggestor implements SuggestionProvider<ClientCommandSource> {
	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ClientCommandSource> context, SuggestionsBuilder builder) {
		for (String s : ClientSettingSaver.getSettings().keySet()) {
			builder.suggest(s);
		}
		return builder.buildFuture();
	}
}
