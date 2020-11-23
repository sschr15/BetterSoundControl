package sschr15.fabricmods.bettersoundcontrol.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;

import net.minecraft.sound.SoundCategory;

import java.lang.reflect.Method;
import java.util.Arrays;

@Mixin(SoundCategory.class)
public abstract class SoundCategoryMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow private static @Final SoundCategory[] field_15255;

    /**
     * @author sschr15
     * @reason because lots of things use enum values() call
     */
    @Overwrite
    public static SoundCategory[] values() {
        try {
            if (new Exception().getStackTrace()[1].getClassName().endsWith("BetterSoundControlServer")) throw new ReflectiveOperationException();

            Method findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            findLoadedClass.setAccessible(true);

            Object classOrNull = findLoadedClass.invoke(SoundCategory.class.getClassLoader(),
                    "sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils");
            if (classOrNull == null) throw new ReflectiveOperationException(); // not repeating code go brr
            if (SoundCategoryUtils.getAllCategories().isEmpty()) throw new ReflectiveOperationException();
            return SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);
        } catch (ReflectiveOperationException e) {
            return Arrays.copyOf(field_15255, field_15255.length);
        }
    }

    static {
        // loading a class go brrr
        //noinspection InstantiationOfUtilityClass
        new SoundCategoryUtils();
    }
}
