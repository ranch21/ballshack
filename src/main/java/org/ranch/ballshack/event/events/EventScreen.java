package org.ranch.ballshack.event.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.ranch.ballshack.event.Event;

public class EventScreen extends Event {
	public Screen screen;

	public static class Render extends EventScreen {
		public final DrawContext drawContext;
		public final int mouseX;
		public final int mouseY;
		public final float tickDelta;

		public Render(Screen screen, DrawContext context, int mouseX, int mouseY, float tickDelta) {
			this.screen = screen;
			this.drawContext = context;
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			this.tickDelta = tickDelta;
		}
	}

	public static class Init extends EventScreen {
		public Init(Screen screen) {
			this.screen = screen;
		}
	}
}
