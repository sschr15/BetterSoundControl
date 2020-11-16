package sschr15.fabricmods.bettersoundcontrol.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sschr15.fabricmods.bettersoundcontrol.client.gui.ScrollingSoundOptionsScreen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.text.Text;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions settings;

    /**
     * <img href="https://cdn.discordapp.com/emojis/707016186281590855.png?v=1"/>
     * @author sschr15
     * @reason scrolling sound options screen
     */
    @SuppressWarnings("OverwriteTarget")
    @Overwrite
    private void method_19829(ButtonWidget widget) {
        this.client.openScreen(new ScrollingSoundOptionsScreen(this, this.settings));
    }

    // bad java
    protected OptionsScreenMixin(Text title) {
        super(title);
    }
}
