package sschr15.fabricmods.bettersoundcontrol.common.network;

import net.minecraft.util.Identifier;

public class NetworkIDs {
    public static final Identifier S2C_REQUEST_CATS = id("request_categories");

    public static final Identifier C2S_CATEGORIES = id("available_categories_client");
    public static final Identifier S2C_CATEGORIES = id("available_categories_server");

    public static final Identifier S2C_PLAY_SOUND = id("play_sound");

    // utility method to make new identifiers
    private static Identifier id(String id) {
        return new Identifier("bettersoundcontrol", id);
    }
}
