package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.windows.IWindow;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutoFitWindow extends Window {

	protected boolean xFit;
	protected boolean yFit;
	protected int xPad;
	protected int yPad;

	public AutoFitWindow(String title, int x, int y, int width, int height, boolean xFit, boolean yFit, int xPad, int yPad) {
		super(title, x, y, width, height);
		this.xFit = xFit;
		this.yFit = yFit;
		this.xPad = xPad;
		this.yPad = yPad;
	}

	@Override
	public void addChild(Window child) {
		super.addChild(child);
		fit();
	}

	protected void fit() {
		if (xFit) fitWidth();
		if (yFit) fitHeight();
	}

	protected void fitHeight() {
		setHeight(getMaxScrollY() + yPad);
	}

	protected void fitWidth() {
		setWidth(getMaxScrollX() + xPad);
	}
}
