package com.axialeaa.glissando.config.gui.widget;

import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import org.jetbrains.annotations.Nullable;

public abstract class UnnavigableWidget extends ClickableWidget {

    public UnnavigableWidget(int size) {
        this(size, size);
    }

    public UnnavigableWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public UnnavigableWidget(int x, int y, int width, int height) {
        super(x, y, width, height, ScreenTexts.EMPTY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    @Override
    public void playDownSound(SoundManager soundManager) {}

    @Override
    public boolean isNarratable() {
        return false;
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }

}