package sschr15.fabricmods.bettersoundcontrol.server;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;
import sschr15.fabricmods.bettersoundcontrol.common.network.NetworkIDs;
import sschr15.fabricmods.bettersoundcontrol.common.util.Ref;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;

import java.util.*;

@Environment(EnvType.SERVER)
public class BetterSoundControlServer implements DedicatedServerModInitializer {
    private static final Map<PlayerEntity, List<SoundCategory>> commonCategoriesByPlayer = new HashMap<>();

    public static List<SoundCategory> getCommonCategories(PlayerEntity player) {
        return commonCategoriesByPlayer.getOrDefault(player, Collections.emptyList());
    }

    @Override
    public void onInitializeServer() {
        Ref.LOGGER.warn("Better Sound Control is still in beta for dedicated servers! Please report" +
                "any and all bugs to @sschr15#4563");

        // loading early!
        Ref.LOGGER.debug(SoundCategory.MASTER.getName());

        ServerSidePacketRegistry.INSTANCE.register(NetworkIDs.C2S_CATEGORIES, (context, b) -> {
            String bufData = b.readString(32767);
            context.getTaskQueue().execute(() -> {
                List<String> playerCats = Arrays.asList(bufData.split(","));
                List<SoundCategory> sharedCats = new ArrayList<>();
                List<String> sharedCatIds = new ArrayList<>();
                SoundCategoryUtils.getAllCategories().forEach((name, category) -> {
                    if (playerCats.contains(name)) {
                        sharedCats.add(category);
                        sharedCatIds.add(name);
                    }
                });
                commonCategoriesByPlayer.put(context.getPlayer(), sharedCats);

                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeString(String.join(",", sharedCatIds));

                ServerSidePacketRegistry.INSTANCE.sendToPlayer(context.getPlayer(), NetworkIDs.S2C_CATEGORIES, packetByteBuf);
            });
        });
    }
}
