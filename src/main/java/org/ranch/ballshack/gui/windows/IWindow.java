package org.ranch.ballshack.gui.windows;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;
import java.util.TreeSet;

public interface IWindow {
	void addChild(Window child);
	List<Window> getChildren();
	List<Window> getAllChildren();

	boolean alwaysOnTop();

	void setParent(IWindow parent);
	IWindow getParent();
	IWindow getRoot();
	WindowScreen getRootScreen();

	int getWidth();
	void setWidth(int width);

	int getHeight();
	void setHeight(int height);

	int getInsideOffsetX();
	void setInsideOffsetX(int insideOffsetX);
	int getInsideOffsetY();
	void setInsideOffsetY(int insideOffsetY);

	int getX();
	void setX(int x);

	int getY();
	void setY(int y);
}
