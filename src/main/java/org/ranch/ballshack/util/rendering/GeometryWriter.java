package org.ranch.ballshack.util.rendering;

import net.minecraft.client.render.BufferBuilder;
import org.joml.Matrix4f;

@FunctionalInterface
public interface GeometryWriter {
	void write(BufferBuilder buffer, Matrix4f matrix);
}
