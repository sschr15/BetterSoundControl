# Better Sound Control

A simple mod to allow other mods to use their own sound categories.

## Installing

To use this alone on the client, download it from [my maven](http://maven.concern.i.ng/sschr15/fabricmods/BetterSoundControl/0.1.0/BetterSoundControl-0.1.0.jar).

## Using in a Development Environment

Add to your `build.gradle` (or `build.gradle.kts`):

```groovy
repositories {
    maven {
        url = "http://maven.concern.i.ng" // surround with urlOf() if using Kotlin DSL
    }
}

dependencies {
    implementation("sschr15.fabricmods:BetterSoundControl:0.1.0")
    
    // Suggested, not required:
    include("sschr15.fabricmods:BetterSoundControl:0.1.0")
}
```

Add to your `fabric.mod.json`:

```json
{
    "entrypoints": {
        "bettersoundcontrol": [
            "com.example.examplemod.MoreSoundCategories"
        ]
    }
}
```

Create a class:

```java
public class MoreSoundCategories implements SoundCategoryEntrypoint {
    public static final SoundCategory CATEGORY = SoundCategoryUtils.registerNewCategory("category");
    // etc...
}
```