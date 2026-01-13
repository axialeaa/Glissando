package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import com.axialeaa.glissando.config.gui.widget.ColorPickerWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class BackgroundOptionsScreen extends ConfiguredBackgroundScreen {

    private static final int COLOR_PICKER_SIZE = 120;
    private static final int COLOR_PICKER_OFFSET = 12;
    private static final int BLUR_BUTTON_WIDTH = 60;

    private final MainConfigurationScreen parentScreen;

    public BackgroundOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.background_options.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int y = this.height / 2 - COLOR_PICKER_SIZE + COLOR_PICKER_OFFSET;

        ColorPickerWidget topColorWidget = new ColorPickerWidget(this.width / 2 - COLOR_PICKER_SIZE - COLOR_PICKER_OFFSET, y, COLOR_PICKER_SIZE, Glissando.clientConfig.backgroundTopColor, this.textRenderer);
        ColorPickerWidget bottomColorWidget = new ColorPickerWidget(this.width / 2 + COLOR_PICKER_OFFSET, y, COLOR_PICKER_SIZE, Glissando.clientConfig.backgroundBottomColor, this.textRenderer);

        topColorWidget.forEachChild(this::addDrawableChild);
        bottomColorWidget.forEachChild(this::addDrawableChild);

        this.addDrawableChild(createCyclingBoolean(SCREEN_MARGIN_SIZE, this.height - SCREEN_MARGIN_SIZE - COMMON_BUTTON_SIZE, BLUR_BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.backgroundBlur));
        this.addDrawableChild(createCenteredCloseButton(ScreenTexts.BACK, this, this.height - SCREEN_MARGIN_SIZE - COMMON_BUTTON_SIZE));
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}
