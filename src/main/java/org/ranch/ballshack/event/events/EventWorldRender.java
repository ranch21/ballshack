package org.ranch.ballshack.event.events;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.event.Event;

public abstract class EventWorldRender extends Event {

	public MatrixStack matrixStack;
	public float tickDelta;

	public static class Post extends EventWorldRender {
		public Post(MatrixStack matrixStack, RenderTickCounter renderTickCounter) {
			this.matrixStack = matrixStack;
			this.tickDelta = renderTickCounter.getTickProgress(true);
		}
	}

	public static class Outline extends EventWorldRender {

		public final boolean translucent;
		public final VertexConsumerProvider.Immediate vertexConsumers;

		public Outline(MatrixStack matrixStack, RenderTickCounter renderTickCounter, boolean translucent, VertexConsumerProvider.Immediate vertexConsumers) {
			this.matrixStack = matrixStack;
			this.tickDelta = renderTickCounter.getTickProgress(true);
			this.translucent = translucent;
			this.vertexConsumers = vertexConsumers;
		}
	}

	public static class Entity extends EventWorldRender {

		public final WorldRenderState state;
		public final OrderedRenderCommandQueue queue;


		public Entity(MatrixStack matrixStack, RenderTickCounter renderTickCounter, WorldRenderState state, OrderedRenderCommandQueue queue) {
			this.matrixStack = matrixStack;
			this.tickDelta = renderTickCounter.getTickProgress(true);
			this.state = state;
			this.queue = queue;
		}
	}
}
