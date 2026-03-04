package org.ranch.ballshack.module.modules.render;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventGamma;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.ModeSetting;
import org.ranch.ballshack.setting.module.settings.NumberSetting;

public class Fullbright extends Module {

	public enum Mode {
		GAMMA,
		NIGHT_VISION
	}

	public ModeSetting<Mode> mode = dGroup.add(new ModeSetting<>("Mode", Mode.GAMMA, Mode.values()));
	public NumberSetting gamma = dGroup.add(new NumberSetting("Gamma", 10).min(0).max(20).step(0.5).depends(() -> mode.getValue() == Mode.GAMMA));

	public Fullbright() {
		super("Fullbright", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mode.getValue() == Mode.NIGHT_VISION) {
			mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 100000, 0));
		} else {
			mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}

	@EventSubscribe
	public void onGamma(EventGamma event) {
		if (mode.getValue() == Mode.GAMMA)
			event.gamma = gamma.getValue();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		if (mode.getValue() == Mode.NIGHT_VISION) {
			mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}
}
