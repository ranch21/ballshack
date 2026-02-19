package org.ranch.ballshack.gui.windows;

import net.minecraft.client.gui.Element;

public interface IWindow {
	void addChild(Window child);

	void setParent(IWindow parent);
	IWindow getParent();
	IWindow getRoot();

	int getWidth();
	void setWidth(int width);

	int getHeight();
	void setHeight(int height);

	int getX();
	void setX(int x);

	int getY();
	void setY(int y);
}
