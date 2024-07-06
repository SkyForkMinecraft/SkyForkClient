//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package alan;


import cn.cedo.PlayerMoveUpdateEvent;
import cn.dxg.EventMotion;
import cn.dxg.LookEvent;
import cn.dxg.RotationUtil;
import org.lwjgl.util.vector.Vector2f;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.movement.JumpEvent;
import org.union4dev.base.events.update.UpdateEvent;

public final class RotationComponent implements Access.InstanceAccess
{
    private static boolean active;
    private static boolean smoothed;
    public static Vector2f rotations;
    public static Vector2f lastRotations;
    public static Vector2f targetRotations;
    public static Vector2f lastServerRotations;
    private static double rotationSpeed;
    private static MovementFix correctMovement;
    
    public static double getRotationDifference(final Vector2f rotation) {
        return getRotationDifference(rotation, RotationComponent.lastServerRotations);
    }
    
    public static double getRotationDifference(final Vector2f a, final Vector2f b) {
        return Math.hypot(getAngleDifference(a.getX(), b.getX()), a.getY() - b.getY());
    }
    
    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static void setRotations(final Vector2f rotations, final double rotationSpeed, final MovementFix correctMovement) {
        RotationComponent.targetRotations = rotations;
        RotationComponent.rotationSpeed = rotationSpeed * 36.0;
        RotationComponent.correctMovement = correctMovement;
        RotationComponent.active = true;
        smooth();
    }
    
    public static void setRotations(final Vector2f rotations, final double rotationSpeed, final MovementFix correctMovement, final boolean noSmooth) {
        RotationComponent.targetRotations = rotations;
        RotationComponent.rotationSpeed = rotationSpeed * 36.0;
        RotationComponent.correctMovement = correctMovement;
        RotationComponent.active = true;
        if (!noSmooth) {
            smooth();
        }
    }
    
    @EventTarget(100)
    public void onUpdateEvent(final UpdateEvent e) {
        if (!RotationComponent.active || RotationComponent.rotations == null || RotationComponent.lastRotations == null || RotationComponent.targetRotations == null || RotationComponent.lastServerRotations == null) {
            RotationComponent.rotations = (RotationComponent.lastRotations = (RotationComponent.targetRotations = (RotationComponent.lastServerRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch))));
        }
        if (RotationComponent.active) {
            smooth();
        }
        if (RotationComponent.correctMovement == MovementFix.BACKWARDS_SPRINT && RotationComponent.active && Math.abs(RotationComponent.rotations.x - Math.toDegrees(MoveUtil.direction())) > 45.0) {
            mc.gameSettings.keyBindSprint.pressed = false;
            mc.thePlayer.setSprinting(false);
        }
    }
    
    @EventTarget(0)
    public void onMoveInput(final PlayerMoveUpdateEvent e) {
        if (RotationComponent.active && RotationComponent.correctMovement == MovementFix.NORMAL && RotationComponent.rotations != null) {
            final float yaw = RotationComponent.rotations.x;
            MoveUtil.fixMovement(e, yaw);
        }
    }
    
    @EventTarget(0)
    public void onLook(final LookEvent e) {
        if (RotationComponent.active && RotationComponent.rotations != null) {
            e.setRotation(RotationComponent.rotations);
        }
    }
    
    @EventTarget(0)
    public void onPlayerMoveUpdateEvent(final PlayerMoveUpdateEvent e) {
        if (RotationComponent.active && (RotationComponent.correctMovement == MovementFix.NORMAL || RotationComponent.correctMovement == MovementFix.TRADITIONAL) && RotationComponent.rotations != null) {
            e.setYaw(RotationComponent.rotations.x);
        }
    }
    
    @EventTarget(0)
    public void onJumpFixEvent(final JumpEvent e) {
        if (RotationComponent.active && (RotationComponent.correctMovement == MovementFix.NORMAL || RotationComponent.correctMovement == MovementFix.TRADITIONAL || RotationComponent.correctMovement == MovementFix.BACKWARDS_SPRINT) && RotationComponent.rotations != null) {
            e.setYaw(RotationComponent.rotations.x);
        }
    }
    
    @EventTarget
    public void onMotionEvent(final EventMotion e) {
        if (e.isPre()) {
            if (RotationComponent.active && RotationComponent.rotations != null) {
                final float yaw = RotationComponent.rotations.x;
                final float pitch = RotationComponent.rotations.y;
                e.setYaw(yaw);
                e.setPitch(pitch);
                mc.thePlayer.renderYawOffset = yaw;
                mc.thePlayer.rotationYawHead = yaw;
                mc.thePlayer.renderPitchHead = pitch;
                RotationComponent.lastServerRotations = new Vector2f(yaw, pitch);
                if (Math.abs((RotationComponent.rotations.x - mc.thePlayer.rotationYaw) % 360.0f) < 1.0f && Math.abs(RotationComponent.rotations.y - mc.thePlayer.rotationPitch) < 1.0f) {
                    RotationComponent.active = false;
                    this.correctDisabledRotations();
                }
                RotationComponent.lastRotations = RotationComponent.rotations;
            }
            else {
                RotationComponent.lastRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            }
            RotationComponent.targetRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            RotationComponent.smoothed = false;
        }
    }
    
    private void correctDisabledRotations() {
        final Vector2f rotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        final Vector2f fixedRotations = RotationUtil.resetRotation(RotationUtil.applySensitivityPatch(rotations, RotationComponent.lastRotations));
        mc.thePlayer.rotationYaw = fixedRotations.x;
        mc.thePlayer.rotationPitch = fixedRotations.y;
    }
    
    public static void smooth() {
        if (!RotationComponent.smoothed) {
            final float lastYaw = RotationComponent.lastRotations.x;
            final float lastPitch = RotationComponent.lastRotations.y;
            final float targetYaw = RotationComponent.targetRotations.x;
            final float targetPitch = RotationComponent.targetRotations.y;
            RotationComponent.rotations = RotationUtil.smooth(new Vector2f(lastYaw, lastPitch), new Vector2f(targetYaw, targetPitch), RotationComponent.rotationSpeed + Math.random() / 2.0);
            if (RotationComponent.correctMovement == MovementFix.NORMAL || RotationComponent.correctMovement == MovementFix.TRADITIONAL) {
                mc.thePlayer.movementYaw = RotationComponent.rotations.x;
            }
            mc.thePlayer.velocityYaw = RotationComponent.rotations.x;
        }
        RotationComponent.smoothed = true;
        mc.entityRenderer.getMouseOver(1.0f);
    }
}