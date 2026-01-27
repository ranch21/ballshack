package org.ranch.ballshack.command.suggestors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.debug.DebugRenderers;

import java.util.concurrent.CompletableFuture;

public class DebugRendererSuggestor implements SuggestionProvider<ClientCommandSource> {
	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
		for (String r : DebugRenderers.getIdList()) {
			builder.suggest(r);
		}
		return builder.buildFuture();
	}
}
