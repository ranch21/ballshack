package org.ranch.ballshack.command.suggestors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.util.Formatters;

import java.util.concurrent.CompletableFuture;

public class FormatSuggestor implements SuggestionProvider<ClientCommandSource> {
	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ClientCommandSource> context, SuggestionsBuilder builder) {
		for (String s : Formatters.FORMATTERS.keySet()) {
			builder.suggest(s);
		}
		return builder.buildFuture();
	}
}
