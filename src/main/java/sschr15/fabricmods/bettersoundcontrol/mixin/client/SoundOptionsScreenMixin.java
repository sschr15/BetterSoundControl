package sschr15.fabricmods.bettersoundcontrol.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;

import net.minecraft.client.gui.screen.options.SoundOptionsScreen;
import net.minecraft.sound.SoundCategory;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;"))
    private SoundCategory[] getSoundCategories() {
        return SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);
    }
}
