package org.ranch.ballshack.debug.renderers;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

public class VecDebugRenderer extends DebugRenderer {

    private Vec3d vec;
    private Vec3d pos;
    private Color color;

    public void setData(Vec3d pos, Vec3d vec, Color color) {
        this.vec = vec;
        this.pos = pos;
        this.color = color;
    }

    @Override
    public void renderGui(DrawContext context) {

    }

    @Override
    public void render3d(Renderer context, MatrixStack matrixStack) {
        context.renderArrow(pos, pos.add(vec), 4, 0.2f, color, matrixStack);
        context.draw(BallsRenderPipelines.QUADS);
    }
}
