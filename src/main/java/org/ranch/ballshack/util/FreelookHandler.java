package org.ranch.ballshack.util;

import net.minecraft.util.math.MathHelper;

public class FreelookHandler {
    public static float yaw = 0F;
    public static float pitch = 0F;

    public static boolean enabled = false;
    public static boolean rotateInput = true;

    public static Rotation getRotation() {
        return new Rotation(yaw, pitch);
    }

    public static void setRotation(Rotation rotation) {
        yaw = rotation.yaw;
        pitch = rotation.pitch;
    }

    public static void updateDirection(double cursorDeltaX, double cursorDeltaY) {
        float f = (float) cursorDeltaY * 0.15F;
        float g = (float) cursorDeltaX * 0.15F;
        pitch = pitch + f;
        yaw = yaw + g;
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
    }
}
