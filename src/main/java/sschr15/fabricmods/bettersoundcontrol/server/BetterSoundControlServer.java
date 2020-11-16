package sschr15.fabricmods.bettersoundcontrol.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sschr15.fabricmods.bettersoundcontrol.common.util.Ref;

@Environment(EnvType.SERVER)
public class BetterSoundControlServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Ref.LOGGER.warn("Better Sound Control is not yet working for dedicated servers!" +
                "Please spam @sschr15#4563 on discord to make it work thank");
    }
}
