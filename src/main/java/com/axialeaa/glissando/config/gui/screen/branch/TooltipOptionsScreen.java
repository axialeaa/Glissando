package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class TooltipOptionsScreen extends ConfiguredBackgroundScreen {

    private final MainConfigurationScreen parentScreen;

    public TooltipOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.tooltip_options.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.addDrawableChild(createCyclingBoolean(10, this.height - SCREEN_MARGIN_SIZE - COMMON_BUTTON_SIZE, 60, COMMON_BUTTON_SIZE, Glissando.clientConfig.enableTooltips));
        this.addDrawableChild(createCenteredCloseButton(ScreenTexts.BACK, this, this.height - SCREEN_MARGIN_SIZE - COMMON_BUTTON_SIZE));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

}
