package sschr15.fabricmods.bettersoundcontrol.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;
import sschr15.fabricmods.bettersoundcontrol.common.BetterSoundControlCategories;
import sschr15.fabricmods.bettersoundcontrol.common.network.NetworkIDs;
import sschr15.fabricmods.bettersoundcontrol.common.util.Ref;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class BetterSoundControl implements ClientModInitializer {
    private static Map<String, SoundCategory> acceptedCategories = new HashMap<>();

    public static SoundCategory getCategory(String id) {
        return acceptedCategories.getOrDefault(id, BetterSoundControlCategories.UNKNOWN);
    }

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(NetworkIDs.S2C_REQUEST_CATS, (context, buffer) -> context.getTaskQueue().execute(() -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeString(SoundCategoryUtils.getAllCategories().values().stream().map(SoundCategory::getName).collect(Collectors.joining(",")));

            ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkIDs.C2S_CATEGORIES, buf);
        }));

        ClientSidePacketRegistry.INSTANCE.register(NetworkIDs.S2C_CATEGORIES, (context, b) -> {
            String bufData = b.readString();
            context.getTaskQueue().execute(() -> {
                for (String id : bufData.split(",")) {
                    acceptedCategories.put(id, SoundCategoryUtils.getCategory(id));
                }

                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, new TranslatableText("bettersoundcontrol.chat.done"), context.getPlayer().getUuid());
            });
        });

        ClientSidePacketRegistry.INSTANCE.register(NetworkIDs.S2C_PLAY_SOUND, (context, buffer) -> {
            Identifier identifier = buffer.readIdentifier();
            String category = buffer.readString();
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();
            float volume = buffer.readFloat();
            float pitch = buffer.readFloat();
            context.getTaskQueue().execute(() -> MinecraftClient.getInstance().getSoundManager().play(new PositionedSoundInstance(
                    identifier, SoundCategoryUtils.getCategory(category), volume, pitch, false, 0,
                    SoundInstance.AttenuationType.LINEAR, x, y, z, false
            )));
        });

        Ref.LOGGER.info("Better Sound Control is loaded!");
    }
}
