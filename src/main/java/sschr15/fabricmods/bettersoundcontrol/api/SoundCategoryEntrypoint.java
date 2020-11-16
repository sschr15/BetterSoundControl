package sschr15.fabricmods.bettersoundcontrol.api;

/**
 * An entrypoint that is run right before the sound category
 * list is read for the first time.
 * <p/>
 * To make life the easiest for other modders, it may be best
 * to use a special class that has a bunch of
 * public static final members, like this:
 * <pre>
 * public class ModSoundCategories {
 *     public static final SoundCategory cat1 = SoundCategoryUtils.registerNewCategory("cat1");
 *     public static final SoundCategory cat2 = SoundCategoryUtils.registerNewCategory("cat2");
 *     public static final SoundCategory cat3 = SoundCategoryUtils.registerNewCategory("cat3");
 *     public static final SoundCategory cat4 = SoundCategoryUtils.registerNewCategory("cat4");
 * }
 * </pre>
 * An easy way to register them all is to simply create
 * a new throwaway object:
 * <pre>
 * public class ModSoundCategoryEntry implements SoundCategoryEntrypoint {
 *      ​@Override
 *      public void onSoundCategoryRegistry() {
 *          new ModSoundCategories();
 *      }
 * }
 * </pre>
 * This will load the object and therefore register all of its categories.
 * <p/>
 * Another possible solution would to make the category class implement
 * this interface directly, as that will also register the categories
 * at the right time. Due to {@link #onSoundCategoryRegistry()} being
 * default, it does not require implementation in this case.
 * <p/>
 * Example:
 * <pre>
 * public class ModSoundCategories implements SoundCategoryEntrypoint {
 *     public static final SoundCategory cat1 = SoundCategoryUtils.registerNewCategory("cat1");
 *     public static final SoundCategory cat2 = SoundCategoryUtils.registerNewCategory("cat2");
 *     public static final SoundCategory cat3 = SoundCategoryUtils.registerNewCategory("cat3");
 *     public static final SoundCategory cat4 = SoundCategoryUtils.registerNewCategory("cat4");
 * }
 * </pre>
 */
public interface SoundCategoryEntrypoint {
    default void onSoundCategoryRegistry() {}
}
