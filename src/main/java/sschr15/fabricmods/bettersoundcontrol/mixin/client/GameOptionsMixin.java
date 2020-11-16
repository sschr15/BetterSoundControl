package sschr15.fabricmods.bettersoundcontrol.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;

import net.minecraft.client.options.GameOptions;
import net.minecraft.sound.SoundCategory;

import java.util.HashMap;
import java.util.Map;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Mutable
    @Shadow @Final private Map<SoundCategory, Float> soundVolumeLevels;

    @Redirect(method = {"load", "write"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;"))
    private SoundCategory[] getSoundCategories() {
        return SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.soundVolumeLevels = new HashMap<>();
        for (SoundCategory category : SoundCategoryUtils.getAllCategories().values()) {
            soundVolumeLevels.put(category, 1.0F);
        }
    }
}
