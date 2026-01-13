package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.widget.ConfigButtonLocationWidget;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import com.axialeaa.glissando.config.option.ConfigButtonPosition;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class ConfigButtonPositionOptionScreen extends ConfiguredBackgroundScreen {

    private static final int CENTER_BUTTON_WIDTH = 80;

    private final MainConfigurationScreen parentScreen;

    public ConfigButtonPositionOptionScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.config_button_position.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();

        for (ConfigButtonPosition position : ConfigButtonPosition.values())
            this.addDrawableChild(new ConfigButtonLocationWidget(this, position));

        new DirectionalLayoutBuilder(
            ButtonWidget
                .builder(Text.translatable("controls.reset"), widget -> {
                    Glissando.clientConfig.configButtonPosition.reset();
                    this.close();
                })
                .size(CENTER_BUTTON_WIDTH, COMMON_BUTTON_SIZE)
                .build(),
            ButtonWidget
                .builder(ScreenTexts.CANCEL, widget -> this.close())
                .size(CENTER_BUTTON_WIDTH, COMMON_BUTTON_SIZE)
                .build()
        )
            .vertical()
            .position(w -> this.width / 2 - CENTER_BUTTON_WIDTH / 2, h -> this.height / 4 + DONE_BUTTON_Y_POS - 100)
            .build()
            .forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.renderTitleText(context);
    }

    private void renderTitleText(DrawContext context) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.getTitle(), this.width / 2, 40, Colors.WHITE);
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}
