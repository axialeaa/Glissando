package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.branch.ConfigButtonPositionOptionScreen;
import com.axialeaa.glissando.config.option.ConfigButtonPosition;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.COMMON_BUTTON_SIZE;

public class ConfigButtonLocationWidget extends ButtonWidget {

    private static final Identifier HIGHLIGHTED_SPRITE = Glissando.id("config/config_button/config_button_location_highlighted");
    private static final Identifier SPRITE = Glissando.id("config/config_button/config_button_location");

    private final ConfigButtonPositionOptionScreen screen;
    private final ConfigButtonPosition position;

    public ConfigButtonLocationWidget(ConfigButtonPositionOptionScreen screen, ConfigButtonPosition position) {
        super(position.getX(screen.width, screen.height), position.getY(screen.width, screen.height), COMMON_BUTTON_SIZE, COMMON_BUTTON_SIZE, ScreenTexts.EMPTY, ConfigButtonLocationWidget::getPressAction, DEFAULT_NARRATION_SUPPLIER);
        this.screen = screen;
        this.position = position;

        this.setTooltip(Tooltip.of(Text.translatable("glissando.option.config_button_position.location")));
    }

    private static void getPressAction(ButtonWidget widget) {
        ConfigButtonLocationWidget configButtonLocationWidget = (ConfigButtonLocationWidget) widget;

        Glissando.clientConfig.configButtonPosition.set(configButtonLocationWidget.position);
        configButtonLocationWidget.screen.close();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.isHovered() ? HIGHLIGHTED_SPRITE : SPRITE, this.getX(), this.getY(), COMMON_BUTTON_SIZE, COMMON_BUTTON_SIZE);
    }

}
