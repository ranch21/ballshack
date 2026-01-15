package org.ranch.ballshack.debug.renderers;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.List;

public class PathDebugRenderer extends DebugRenderer {

    private List<? extends Position> path;
    private Color color;

    public void setData(List<? extends Position> path, Color color) {
        this.path = path;
        this.color = color;
    }

    @Override
    public void renderGui(DrawContext context) {

    }

    @Override
    public void render3d(Renderer context, MatrixStack matrixStack) {
        if (path == null)
            return;
        float size = 0.2f;
        for (Position pos : path) {
            Box box = new Box(pos.getX() - size, pos.getY() - size, pos.getZ() - size, pos.getX() + size, pos.getY() + size, pos.getZ() + size);
            context.renderCube(box, color, matrixStack);

        }
        context.draw(BallsRenderPipelines.QUADS);
    }
}
