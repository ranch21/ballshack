package org.ranch.ballshack.debug.renderers;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.windows.IWindow;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowsDebugRenderer extends DebugRenderer {

	IWindow window;

	public void setData(IWindow rootWindow) {
		this.window = rootWindow;
	}

	@EventSubscribe
	public void onWorldRender(EventScreen.Render event) {
		if (window == null) return;
		List<IWindow> touching = new ArrayList<>();
		for (IWindow win : window.getAllChildren()) {
			if (GuiUtil.mouseOverlap(event.mouseX, event.mouseY, win.getX(), win.getY(), win.getWidth(), win.getHeight())) {
				touching.add(win);
			}
			if (win instanceof Window win2 && win2.isFocused()) {
				DrawUtil.drawOutline(event.drawContext, win.getX(), win.getY(), win.getWidth(), win.getHeight(), Color.YELLOW, Color.YELLOW);
			}
		}
		int i = 0;
		for (IWindow iwin : touching) {
			if (i == touching.size() - 1) {
				DrawUtil.drawOutline(event.drawContext, iwin.getX(), iwin.getY(), iwin.getWidth(), iwin.getHeight(), Color.BLUE, Color.BLUE);
				for (IWindow kid : iwin.getChildren()) {
					DrawUtil.drawOutline(event.drawContext, kid.getX(), kid.getY(), kid.getWidth(), kid.getHeight(), Color.GREEN, Color.GREEN);
				}
			} else {
				DrawUtil.drawOutline(event.drawContext, iwin.getX(), iwin.getY(), iwin.getWidth(), iwin.getHeight(), Color.RED, Color.RED);
			}
			i++;
		}
	}
}
