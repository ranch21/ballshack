package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;
import org.ranch.ballshack.util.Rotation;

public class EventCameraRot extends Event {

    public Rotation rot;

    public final Rotation origRot;

    public EventCameraRot(Rotation rot) {
        this.origRot = rot;
        this.rot = rot;
    }
}
