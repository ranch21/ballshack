package org.ranch.ballshack.gui.renderers;

import net.minecraft.text.Text;
import org.ranch.ballshack.util.rendering.BallColor;

//todo stop using the drawcontext ffs
public interface GuiRenderer {

	void fill(float x1, float y1, float x2, float y2, BallColor color);

	void border(float x1, float y1, float x2, float y2, BallColor color);

	void line(float x1, float y1, float x2, float y2, BallColor color);

	void text(Text text, boolean shadow, BallColor color);

	void text(String text, boolean shadow, BallColor color);
}
