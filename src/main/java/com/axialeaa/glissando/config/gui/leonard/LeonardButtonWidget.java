package com.axialeaa.glissando.config.gui.leonard;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.screen.branch.DebugOptionsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class LeonardButtonWidget extends ButtonWidget {

    private static final Identifier DEBUG_OPTIONS_TEXTURE = Glissando.id("config/options/debug_options");
    private boolean ctrlDown;

    public LeonardButtonWidget(MainConfigurationScreen parentScreen) {
        super(0, 0, COMMON_BUTTON_SIZE, COMMON_BUTTON_SIZE, Text.translatable("glissando.screen.debug_options.title"), widget -> MinecraftClient.getInstance().setScreen(new DebugOptionsScreen(parentScreen)), DEFAULT_NARRATION_SUPPLIER);
        this.setTooltip(Tooltip.of(this.getMessage()));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);

        int textureX = this.getX() + BUTTON_TEXTURE_MARGIN;
        int textureY = this.getY() + BUTTON_TEXTURE_MARGIN;

         if (!Leonard.active)
             context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, DEBUG_OPTIONS_TEXTURE, textureX, textureY, COMMON_TEXTURE_SIZE, COMMON_TEXTURE_SIZE);

        boolean ctrlDown = Screen.hasControlDown() && this.isSelected();

        if (this.ctrlDown == ctrlDown)
            return;

        this.ctrlDown = ctrlDown;

        if (ctrlDown)
            Leonard.processInput();
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {}

}