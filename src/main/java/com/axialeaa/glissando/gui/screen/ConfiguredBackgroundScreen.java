package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.Glissando;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class ConfiguredBackgroundScreen extends Screen {

    public ConfiguredBackgroundScreen(Text title) {
        super(title);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (this.client != null && this.client.world == null)
            this.renderPanoramaBackground(context, deltaTicks);

        if (Glissando.clientConfig.backgroundBlur.get())
            this.applyBlur(context);

        this.renderGradientBackground(context);
    }

    private void renderGradientBackground(DrawContext context) {
        context.fillGradient(
            0, 0,
            this.width, this.height,
            Glissando.clientConfig.backgroundTopColor.get(),
            Glissando.clientConfig.backgroundBottomColor.get()
        );
    }

}
