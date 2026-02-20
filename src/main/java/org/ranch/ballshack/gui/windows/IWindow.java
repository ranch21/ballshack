package org.ranch.ballshack.gui.windows;

import net.minecraft.client.gui.Element;

import java.util.List;

public interface IWindow {
	void addChild(Window child);
	List<Window> getChildren();

	void setParent(IWindow parent);
	IWindow getParent();
	IWindow getRoot();

	int getWidth();
	void setWidth(int width);

	int getHeight();
	void setHeight(int height);

	int getInsideOffsetX();
	int getInsideOffsetY();

	int getX();
	void setX(int x);

	int getY();
	void setY(int y);
}
