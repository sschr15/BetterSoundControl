package sschr15.fabricmods.bettersoundcontrol.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

/**
 * This Mixin is simply to make client-side methods server-side as well.
 * If using in code, call the methods normally.
 */
@SuppressWarnings("unused")
@Mixin(PlaySoundIdS2CPacket.class)
public abstract class PlaySoundIdS2CPacketServerAccessor {
    @Shadow private Identifier id;
    @Shadow private SoundCategory category;
    @Shadow private int fixedX;
    @Shadow private int fixedY;
    @Shadow private int fixedZ;
    @Shadow private float volume;
    @Shadow private float pitch;

    public Identifier getSoundId() {
        return this.id;
    }

    public SoundCategory getCategory() {
        return this.category;
    }

    public double getX() {
        return (float)this.fixedX / 8.0F;
    }

    public double getY() {
        return (float)this.fixedY / 8.0F;
    }

    public double getZ() {
        return (float)this.fixedZ / 8.0F;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }
}
