package sschr15.fabricmods.bettersoundcontrol.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sschr15.fabricmods.bettersoundcontrol.common.util.Ref;

@Environment(EnvType.CLIENT)
public class BetterSoundControl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Ref.LOGGER.info("Better Sound Control is loaded!");
    }
}
