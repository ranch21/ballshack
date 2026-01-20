package org.ranch.ballshack.mixin;

import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.util.PlayerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkinTextures.class)
public class SkinTexturesMixin {
	@Inject(method = "cape", at = @At("HEAD"), cancellable = true)
	public void taw(CallbackInfoReturnable<AssetInfo.TextureAsset> cir) {
		String capeName = PlayerUtil.getCape();
		if (capeName != null) {
			AssetInfo.TextureAsset cape = new AssetInfo.TextureAsset() {
				@Override
				public Identifier texturePath() {
					return Identifier.of(BallsHack.ID, "textures/capes/" + capeName + ".png");
				}

				@Override
				public Identifier id() {
					return texturePath();
				}
			};

			cir.setReturnValue(cape);
		}
	}
}
