package sschr15.fabricmods.bettersoundcontrol.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sschr15.fabricmods.bettersoundcontrol.common.BetterSoundControlCategories;

import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;

@Mixin(WorldListWidget.Entry.class)
public abstract class WorldListWidgetEntryMixin {
    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/PositionedSoundInstance;master(Lnet/minecraft/sound/SoundEvent;F)Lnet/minecraft/client/sound/PositionedSoundInstance;"))
    private PositionedSoundInstance toGuiCategory(SoundEvent uiButtonClick, float pitch) {
        return new PositionedSoundInstance(
                uiButtonClick.getId(), // identifier
                BetterSoundControlCategories.GUI, // category
                0.25f,
                pitch,
                false,
                0,
                SoundInstance.AttenuationType.NONE,
                0, 0, 0, // x, y, z
                true // looping?
        );
    }
}
