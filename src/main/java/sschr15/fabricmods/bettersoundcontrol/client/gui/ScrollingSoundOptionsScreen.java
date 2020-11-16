package sschr15.fabricmods.bettersoundcontrol.client.gui;

import sschr15.fabricmods.bettersoundcontrol.api.SoundCategoryUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;

public class ScrollingSoundOptionsScreen extends GameOptionsScreen {
    private ButtonListWidget list;

    public ScrollingSoundOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, new TranslatableText("options.sounds.title"));
    }

    @Override
    protected void init() {
//        this.addButton(new SoundSliderWidget(this.client, this.width / 2 - 155, this.height / 6 - 12, SoundCategory.MASTER, 310));
        SoundCategory[] categories = SoundCategoryUtils.getAllCategories().values().toArray(new SoundCategory[0]);

//        for (SoundCategory soundCategory : categories) {
//            if (soundCategory != SoundCategory.MASTER) {
//                this.addButton(new SoundSliderWidget(this.client, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i++ >> 1), soundCategory, 150));
//            }
//        }

        this.list = new ButtonListWidget(this.client, this.width, this.height, 43, this.height - 29 - this.getTextHeight(), 25);
        this.list.addSingleOptionEntry(new SoundOption(SoundCategory.MASTER));
        this.list.addAll(Arrays.stream(categories).filter(category -> category != SoundCategory.MASTER).map(SoundOption::new).toArray(SoundOption[]::new));
        this.addChild(list);

        this.addButton(new OptionButtonWidget(this.width / 2 - 75, this.height - 50, 150, 20,
                Option.SUBTITLES, Option.SUBTITLES.getDisplayString(this.gameOptions), (button) -> {
            Option.SUBTITLES.toggle(this.client.options);
            button.setMessage(Option.SUBTITLES.getDisplayString(this.client.options));
            this.client.options.write();
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 25, 200, 20, ScreenTexts.DONE, (button) -> this.client.openScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private int getTextHeight()
    {
        return (5 + this.textRenderer.fontHeight) * 3 + 5;
    }
}
