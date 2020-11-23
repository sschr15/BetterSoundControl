package sschr15.fabricmods.bettersoundcontrol.mixin.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;
import sschr15.fabricmods.bettersoundcontrol.common.BetterSoundControlCategories;
import sschr15.fabricmods.bettersoundcontrol.common.network.NetworkIDs;
import sschr15.fabricmods.bettersoundcontrol.server.BetterSoundControlServer;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

@Mixin(PlaySoundCommand.class)
public abstract class PlaySoundCommandMixin {

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;"))
    private static SoundCategory[] getSoundCategories() {
        //TODO server -> client when supported
        // unable to edit this on the fly apparently :(
        return SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);
    }

    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private static void sendExtraPacket(ServerPlayNetworkHandler handler, Packet<?> prev) {
        PlaySoundIdS2CPacket packet = (PlaySoundIdS2CPacket) prev;

        if (!SoundCategoryUtils.DEFAULTS.contains(packet.getCategory())) {
            if (BetterSoundControlServer.getCommonCategories(handler.player).isEmpty()) {
                // Player either doesn't have the mod or something is going *horribly* wrong, so let's play to master!
                handler.sendPacket(new PlaySoundIdS2CPacket(packet.getSoundId(), SoundCategory.MASTER,
                        new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getVolume(), packet.getPitch()));
            } else if (BetterSoundControlServer.getCommonCategories(handler.player).contains(packet.getCategory())) {
                // Player has the sound category! Yay!
                PacketByteBuf soundPacket = new PacketByteBuf(Unpooled.buffer())
                        .writeIdentifier(packet.getSoundId())
                        .writeString(packet.getCategory().getName());
                soundPacket.writeDouble(packet.getX());
                soundPacket.writeDouble(packet.getY());
                soundPacket.writeDouble(packet.getZ());
                soundPacket.writeFloat(packet.getVolume());
                soundPacket.writeFloat(packet.getPitch());

                ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.player, NetworkIDs.S2C_PLAY_SOUND, soundPacket);
            } else {
                // Player has BSC but might not have the specific category, so play in unknown instead
                handler.sendPacket(new PlaySoundIdS2CPacket(packet.getSoundId(), BetterSoundControlCategories.UNKNOWN,
                        new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getVolume(), packet.getPitch()));
            }
        } else handler.sendPacket(packet); // if the category exists in vanilla, why try playing with it?
    }
}
