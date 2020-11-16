package sschr15.fabricmods.bettersoundcontrol.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.SoundSliderWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.sound.SoundCategory;

public class SoundOption extends Option {
    private final SoundCategory category;

    public SoundOption(SoundCategory category) {
        super("soundCategory." + category.getName());
        this.category = category;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        return new SoundSliderWidget(MinecraftClient.getInstance(), x, y, this.category, width);
    }
}
