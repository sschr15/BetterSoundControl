package sschr15.fabricmods.bettersoundcontrol.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;

import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.sound.SoundCategory;

@Mixin(PlaySoundCommand.class)
public abstract class PlaySoundCommandMixin {
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;"))
    private static SoundCategory[] getSoundCategories() {
        //TODO server -> client when supported
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) return SoundCategory.values();
        return SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);
    }
}
