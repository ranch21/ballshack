package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Arm;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.mixin.InputAccessor;

import java.util.ArrayList;
import java.util.List;

public class EntitySim {

    private static MinecraftClient mc = BallsHack.mc;

    public static record PlayerPoint(Vec3d position, Vec3d velocity, boolean onGround) implements Position {

        @Override
        public double getX() {
            return position.getX();
        }

        @Override
        public double getY() {
            return position.getY();
        }

        @Override
        public double getZ() {
            return position.getZ();
        }
    }

    private static class FakeInput extends KeyboardInput {

        private Input copiedInput;

        public FakeInput(Input origInput) {
            super(null); //LOLZ

            boolean forward = origInput.playerInput.forward();
            boolean backward = origInput.playerInput.backward();
            boolean left = origInput.playerInput.left();
            boolean right = origInput.playerInput.right();
            boolean jump = origInput.playerInput.jump();
            boolean sneak = origInput.playerInput.sneak();
            boolean sprint = origInput.playerInput.sprint();
            // NOT AGAIIINNNNNNWNDBWA VGHFEJHNKJ
            copiedInput = new Input();
            ((InputAccessor) copiedInput).setMovementVector(((InputAccessor) origInput).getMovementVector());
            copiedInput.playerInput = new PlayerInput(forward,backward,left,right,jump,sneak,sprint);
        }

        @Override
        public void tick() {
            // goot
        }
    }

    public static class FakePlayer extends LivingEntity {

        FakeInput input;

        protected FakePlayer(ClientWorld world, ClientPlayerEntity original) {
            super(EntityType.PLAYER, world);
            copyPositionAndRotation(original);
            Vec3d origVel = original.getVelocity();
            setVelocity(new Vec3d(origVel.getX(),origVel.getY(),origVel.getZ()));
            setSprinting(original.isSprinting());
            setSneaking(original.isSneaking());
            setOnGround(original.isOnGround());
            input = new FakeInput(original.input);
        }

        @Override
        public Arm getMainArm() {
            return null;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public void tickMovement() {
            super.tickMovement();
        }

        @Override
        public void tickMovementInput() {
            Vec2f vec2f = this.applyMovementSpeedFactors(this.input.getMovementInput());
            this.sidewaysSpeed = vec2f.x;
            this.forwardSpeed = vec2f.y;
            this.jumping = this.input.playerInput.jump();
        }

        private Vec2f applyMovementSpeedFactors(Vec2f input) {
            if (input.lengthSquared() == 0.0F) {
                return input;
            } else {
                Vec2f vec2f = input.multiply(0.98F);
                if (this.isUsingItem() && !this.hasVehicle()) {
                    vec2f = vec2f.multiply(0.2F);
                }

                if (this.shouldSlowDown()) {
                    float f = (float)this.getAttributeValue(EntityAttributes.SNEAKING_SPEED);
                    vec2f = vec2f.multiply(f);
                }

                return applyDirectionalMovementSpeedFactors(vec2f);
            }
        }

        private static Vec2f applyDirectionalMovementSpeedFactors(Vec2f vec) {
            float f = vec.length();
            if (f <= 0.0F) {
                return vec;
            } else {
                Vec2f vec2f = vec.multiply(1.0F / f);
                float g = getDirectionalMovementSpeedMultiplier(vec2f);
                float h = Math.min(f * g, 1.0F);
                return vec2f.multiply(h);
            }
        }

        private static float getDirectionalMovementSpeedMultiplier(Vec2f vec) {
            float f = Math.abs(vec.x);
            float g = Math.abs(vec.y);
            float h = g > f ? f / g : g / f;
            return MathHelper.sqrt(1.0F + MathHelper.square(h));
        }

        public boolean shouldSlowDown() {
            return this.isInSneakingPose() || this.isCrawling();
        }

        @Override
        public boolean canMoveVoluntarily() {
            return true;
        }

        @Override
        public boolean canActVoluntarily() {
            return true;
        }

        @Override
        protected boolean isImmobile() {
            return false;
        }

        @Override
        public boolean isDead() {
            return false;
        }
    }

    //TODO fix it not moving while on ground

    public static List<PlayerPoint> simulatePlayer(ClientPlayerEntity player, int ticks) {
        FakePlayer fakePlayer = new FakePlayer(mc.world, player);
        List<PlayerPoint> points = new ArrayList<>();
        for (int i = 0; i < ticks; i++) {
            fakePlayer.tick();
            points.add(new PlayerPoint(fakePlayer.getEntityPos(), fakePlayer.getVelocity(), fakePlayer.isOnGround()));
        }
        return points;
    }
}