package org.ranch.ballshack.module.modules;

import com.google.common.collect.Streams;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;

import java.util.Comparator;
import java.util.stream.Stream;

public class ModuleList extends Module {
	public ModuleList() {
		super("ModuleList", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender event) {

		Stream<Module> modules = Streams.stream(ModuleManager.getModules());

		Comparator<Module> comparator = Comparator.comparing(m -> m.getName().length());

		modules = modules.filter(Module::isEnabled).sorted(comparator.reversed());

		DrawContext context = event.drawContext;
		TextRenderer textR = mc.textRenderer;

		int x = 5;
		int y = 5;

		for (Module m : modules.toList()) {
			context.drawTextWithShadow(textR, m.getName(), x, y, 0xFFFFFFFF);
			y += textR.fontHeight;
		}
	}
}
