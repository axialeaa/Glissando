package com.axialeaa.glissando.config.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.branch.PulseAnimationOptionsScreen;
import com.axialeaa.glissando.util.AnimationMath;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.SCREEN_MARGIN_SIZE;

public class WaveVisualizerWidget extends UnnavigableWidget {

    private static final Identifier BACKGROUND_TEXTURE = Glissando.id("config/pulse_animation_visualizer/background");
    private static final int CURVE_THICKNESS = 1;
    private static final int CURVE_START_HEIGHT = 6;
    private static final int BOUNCY_SQUARE_SIZE = 30;
    private final PulseAnimationOptionsScreen screen;

    public WaveVisualizerWidget(PulseAnimationOptionsScreen screen, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.screen = screen;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.getX(), this.getY(), this.width, this.height);

        long ticksSinceStarted = this.screen.ticksOpen - this.screen.lastPreviewedTicks;
        AnimationMath.Pulse pulse = new AnimationMath.Pulse(ticksSinceStarted);

        boolean active = pulse.active(tickProgress);
        this.screen.isPreviewing = active;

        if (active) {
            this.renderPlayhead(context, pulse, tickProgress);

            TextRenderer textRenderer = this.screen.getTextRenderer();
            Text text = pulse.getText(tickProgress);

            context.drawText(textRenderer, text, this.getX() + this.width / 2 - textRenderer.getWidth(text) / 2, this.getY() + SCREEN_MARGIN_SIZE, Colors.DARK_GRAY, false);
        }

        this.renderCurve(context);
        this.renderBouncySquare(context, pulse, 0);
    }

    private void renderCurve(DrawContext context) {
        AnimationMath.Pulse pulse = new AnimationMath.Pulse(0);

        int lastHeight = 0;

        for (int i = 0; i < this.width - CURVE_THICKNESS - 1; i++) {
            int x = i + this.getX() + 1;
            int y = MathHelper.ceil(this.height * -pulse.wavePoint((float) i / this.width)) + this.getY();

            y += this.height - CURVE_START_HEIGHT;

            int minY = y;
            int maxY = y - CURVE_THICKNESS;

            if (i > 0) {
                minY = Math.max(lastHeight, minY);
                maxY = Math.min(lastHeight + CURVE_THICKNESS, maxY);
            }

            context.fill(x, minY, x + CURVE_THICKNESS, maxY, Colors.LIGHT_RED);
            lastHeight = y - CURVE_THICKNESS;
        }
    }

    private void renderBouncySquare(DrawContext context, AnimationMath.Pulse pulse, float tickProgress) {
        int bouncySquareSize = Math.round(BOUNCY_SQUARE_SIZE * pulse.scalar(tickProgress));

        int x = this.getX() + this.width - SCREEN_MARGIN_SIZE - 1;
        int y = this.getY() + SCREEN_MARGIN_SIZE + 1;

        context.drawBorder(x - bouncySquareSize, y, bouncySquareSize, bouncySquareSize, Colors.LIGHT_YELLOW);
    }

    private void renderPlayhead(DrawContext context, AnimationMath.Pulse pulse, float tickProgress) {
        int i = MathHelper.lerp(pulse.animationProgress(tickProgress), 0, this.width - 2);

        int x = i + this.getX() + 1;
        int minY = this.getY() + this.height - 1;
        int maxY = MathHelper.ceil(this.height * -pulse.wavePoint((float) i / this.width)) + this.getY();

        maxY += this.height - CURVE_START_HEIGHT - 2;

        context.drawHorizontalLine(this.getX() + 1, x, maxY + 1, Colors.DARK_GRAY);
        context.drawVerticalLine(x, minY, maxY, Colors.CYAN);
    }

}