package org.ranch.ballshack.event.events;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.ranch.ballshack.event.Event;

public abstract class EventFog extends Event {

	public static class Submersion extends EventFog {
		public final CameraSubmersionType origSubmersionType;
		public CameraSubmersionType submersionType;

		public Submersion(CameraSubmersionType submersionType) {
			this.origSubmersionType = this.submersionType = submersionType;
		}
	}
}
