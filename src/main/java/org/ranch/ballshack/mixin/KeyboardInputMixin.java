package org.ranch.ballshack.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.util.FreelookHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        ClientPlayerEntity player = BallsHack.mc.player;
        Input input = player.input;

        EventPlayerInput inputEvent = new EventPlayerInput(input.playerInput);
        BallsHack.eventBus.post(inputEvent);
        if (inputEvent.isCancelled()) {
            input.playerInput = new PlayerInput(false,false,false,false,false,false,false);
        } else {
            input.playerInput = inputEvent.getInput();
        }

        EventPlayerMovementVector vecEvent = new EventPlayerMovementVector(input.getMovementInput());
        BallsHack.eventBus.post(vecEvent);
        if (vecEvent.isCancelled()) {
            ((InputAccessor) input).setMovementVector(new Vec2f(0, 0));
            return;
        }

        ((InputAccessor) input).setMovementVector(vecEvent.movement);
        if (BallsHack.mc.player == null || !FreelookHandler.enabled || !FreelookHandler.rotateInput)
            return;
        Vec3d movement = new Vec3d(input.getMovementInput().x, 0, input.getMovementInput().y);
        movement = movement.rotateY((float) Math.toRadians(player.getYaw() - FreelookHandler.yaw));
        ((InputAccessor) input).setMovementVector(new Vec2f((float) movement.x, (float) movement.z));
    }
}
