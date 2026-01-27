package org.ranch.ballshack.command.suggestors;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;

import java.util.concurrent.CompletableFuture;

import static org.ranch.ballshack.Constants.mc;

public class PlayerSuggestor implements SuggestionProvider<ClientCommandSource> {
	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ClientCommandSource> context, SuggestionsBuilder builder) {
		for (PlayerListEntry p : mc.player.networkHandler.getPlayerList()) {
			GameProfile profile = p.getProfile();
			builder.suggest(profile.name());
		}
		return builder.buildFuture();
	}
}
