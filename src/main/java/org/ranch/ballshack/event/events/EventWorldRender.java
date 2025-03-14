package org.ranch.ballshack.event.events;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.event.Event;

public class EventWorldRender extends Event {

	public MatrixStack matrixStack;
	public float tickDelta;

	public static class Post extends EventWorldRender {
		public Post(MatrixStack matrixStack, RenderTickCounter renderTickCounter) {
			this.matrixStack = matrixStack;
			this.tickDelta = renderTickCounter.getTickDelta(true);
		}
	}

	public static class Outline extends EventWorldRender {

		public boolean translucent;
		public VertexConsumerProvider.Immediate vertexConsumers;

		public Outline(MatrixStack matrixStack, RenderTickCounter renderTickCounter, boolean translucent, VertexConsumerProvider.Immediate vertexConsumers) {
			this.matrixStack = matrixStack;
			this.tickDelta = renderTickCounter.getTickDelta(true);
			this.translucent = translucent;
			this.vertexConsumers = vertexConsumers;
		}
	}
}
