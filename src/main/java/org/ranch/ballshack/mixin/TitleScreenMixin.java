package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.windows.clickgui.ClickGuiScreen;
import org.ranch.ballshack.gui.scanner.ServerScannerScreen;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Shadow
	protected abstract Text getMultiplayerDisabledText();

	@Inject(method = "addNormalWidgets", at = @At(value = "RETURN"), cancellable = true)
	private void addNormalWidgets(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
		Text text = this.getMultiplayerDisabledText();
		boolean bl = text == null;
		Tooltip tooltip = text != null ? Tooltip.of(text) : null;
		(addDrawableChild(ButtonWidget.builder(Text.literal("Server Scanner"), (button) -> {
			Screen screen = new ServerScannerScreen((TitleScreen) (Object) this);
			BallsHack.mc.setScreen(screen);
		}).dimensions(this.width / 2 - 100, y += spacingY, 98, 20).tooltip(tooltip).build())).active = bl;
		(addDrawableChild(ButtonWidget.builder(Text.literal("ClickGUI"), (button) -> {
			ModuleManager.getModuleByClass(ClickGui.class).onEnable();
		}).dimensions(this.width / 2 + 2, y, 98, 20).tooltip(tooltip).build())).active = bl;

		cir.setReturnValue(y);
	}
}
