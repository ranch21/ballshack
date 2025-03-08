package org.ranch.ballshack.event.events;

import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.event.Event;

public class EventWorldRender extends Event {

	//public final Matrix4f positionMatrix;
	//public final Matrix4f projectionMatrix;
	public MatrixStack matrixStack;
	public final float tickDelta;
	//public final VertexConsumerProvider consumers;

	public EventWorldRender(MatrixStack matrixStack, float tickDelta) {
		//this.positionMatrix = positionMatrix;
		//this.projectionMatrix = projectionMatrix;
		this.matrixStack = matrixStack;
		this.tickDelta = tickDelta;
		//this.consumers = consumers;
	}
}
