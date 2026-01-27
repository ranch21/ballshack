package org.ranch.ballshack.command.suggestors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;

import java.util.concurrent.CompletableFuture;

public class ModuleSuggestor implements SuggestionProvider<ClientCommandSource> {
	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ClientCommandSource> context, SuggestionsBuilder builder) {
		for (Module m : ModuleManager.getModules()) {
			builder.suggest(m.getName().toLowerCase());
		}
		return builder.buildFuture();
	}
}
