package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.util.PlayerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Mixin(PlayerSkinProvider.class)
public class PlayerSkinProviderMixin {

	@ModifyReturnValue(
			method = "fetchSkinTextures(Ljava/util/UUID;Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)Ljava/util/concurrent/CompletableFuture;",
			at = @At("RETURN")
	)
	private CompletableFuture<SkinTextures> injectLocalCape(CompletableFuture<SkinTextures> original, @Local(argsOnly = true) UUID uuid) {

		String capeName = PlayerUtil.getCape(uuid);
		if (capeName == null) return original;

		return original.thenApply(original2 -> {

			Identifier id = Identifier.of(
					BallsHack.ID,
					"textures/capes/" + capeName + ".png"
			);

			AssetInfo.TextureAsset cape = new AssetInfo.TextureAsset() {
				@Override public Identifier texturePath() { return id; }
				@Override public Identifier id() { return id; }
			};

			try {
				return new SkinTextures(
						original.get().body(),
						cape,
						original.get().elytra(),
						original.get().model(),
						original.get().secure()
				);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
