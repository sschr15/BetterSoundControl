package sschr15.fabricmods.bettersoundcontrol.api;

import com.google.common.collect.ImmutableList;
import net.fabricmc.loader.api.FabricLoader;
import net.gudenau.lib.unsafe.Unsafe;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import sschr15.fabricmods.bettersoundcontrol.common.BetterSoundControlCategories;
import sschr15.fabricmods.bettersoundcontrol.common.util.Ref;

import net.minecraft.sound.SoundCategory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for adding more sound categories.
 * @see #registerNewCategory(String) Registering a new sound category
 * @see #getCategory(String) Getting a category
 */
public class SoundCategoryUtils {
    private static final Map<String, SoundCategory> CUSTOM_CATEGORIES = new HashMap<>();

    public static final List<SoundCategory> DEFAULTS;

    private static final Field ORDINAL;
    private static final Field NAME;
    private static final Field SC_NAME;

    // Due to execution order, it's better to put this before other stuff.
    static {
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);

            ORDINAL = Enum.class.getDeclaredField("ordinal");
            NAME    = Enum.class.getDeclaredField("name");
            SC_NAME = SoundCategory.class.getDeclaredField("name");

            ORDINAL.setAccessible(true);
            NAME.setAccessible(true);
            SC_NAME.setAccessible(true);

            modifiers.setInt(ORDINAL, ORDINAL.getModifiers() & ~Opcodes.ACC_FINAL);
            modifiers.setInt(NAME, NAME.getModifiers() & ~Opcodes.ACC_FINAL);
            modifiers.setInt(SC_NAME, SC_NAME.getModifiers() & ~Opcodes.ACC_FINAL);

            Field byName = SoundCategory.class.getDeclaredField("BY_NAME");
            byName.setAccessible(true);

            //noinspection unchecked
            CUSTOM_CATEGORIES.putAll((Map<String, SoundCategory>) byName.get(null));
            //noinspection unchecked
            DEFAULTS = ImmutableList.copyOf(((Map<String, SoundCategory>) byName.get(null)).values());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        FabricLoader.getInstance().getEntrypoints("bettersoundcontrol", SoundCategoryEntrypoint.class)
                .forEach(SoundCategoryEntrypoint::onSoundCategoryRegistry);
    }

    /**
     * Register a new sound category. This also adds it to a map in order
     * for Minecraft to get all the sound categories.
     * @param id the category's string-based ID
     * @return a new sound category, or {@code null} if it failed or id is {@code null}
     */
     public static SoundCategory registerNewCategory(String id) {
         if (id == null) return null;
         if (CUSTOM_CATEGORIES.containsKey(id)) return CUSTOM_CATEGORIES.get(id);
         try {
             SoundCategory soundCategory = newInstance(id);
             CUSTOM_CATEGORIES.put(id, soundCategory);
             return soundCategory;
         } catch (ReflectiveOperationException e) {
             Ref.LOGGER.error("Unable to register new sound category!", e);
             return null;
         }
     }

    /**
     * Get a sound category by ID.
     * <p/>
     * You can also get Minecraft's sound categories using this.
     * @param id the category's string-based ID
     * @return the category, or {@link BetterSoundControlCategories#UNKNOWN} if it doesn't exist.
     */
     @NotNull
     public static SoundCategory getCategory(String id) {
         return CUSTOM_CATEGORIES.getOrDefault(id, BetterSoundControlCategories.UNKNOWN);
     }

     public static Map<String, SoundCategory> getAllCategories() {
         return new HashMap<>(CUSTOM_CATEGORIES);
     }

     private static SoundCategory newInstance(String id) throws ReflectiveOperationException {
        SoundCategory soundCategory = Unsafe.allocateInstance(SoundCategory.class);
        ORDINAL.setInt(soundCategory, CUSTOM_CATEGORIES.size());
        NAME.set(soundCategory, id.toUpperCase().replaceAll("[^A-Z]", "_"));
        SC_NAME.set(soundCategory, id);

        return soundCategory;
     }
}
