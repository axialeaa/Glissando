package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import com.axialeaa.glissando.config.gui.widget.NumberSliderOptionWidget;
import com.axialeaa.glissando.config.gui.widget.WaveVisualizerWidget;
import com.axialeaa.glissando.config.option.Option;
import com.axialeaa.glissando.gui.screen.ConfiguredBackgroundScreen;
import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class PulseAnimationOptionsScreen extends ConfiguredBackgroundScreen {

    private static final int MULTILINE_MAX_ROWS = 4;
    private final MainConfigurationScreen parentScreen;

    public long ticksOpen;
    public long lastPreviewedTicks;
    public boolean isPreviewing = false;

    public PulseAnimationOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.pulse_animation_options.title"));
        this.parentScreen = parentScreen;
        this.resetPreviewState();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks);
    }

    private void resetPreviewState() {
        this.lastPreviewedTicks = Integer.MAX_VALUE;
    }

    private void startPreview() {
        this.lastPreviewedTicks = this.ticksOpen;
    }

    @Override
    protected void init() {
        WaveVisualizerWidget waveVisualizerWidget = new WaveVisualizerWidget(this, SCREEN_MARGIN_SIZE, SCREEN_MARGIN_SIZE, this.width / 3 * 2 - SCREEN_MARGIN_SIZE * 2, this.height - SCREEN_MARGIN_SIZE * 2);
        this.addDrawableChild(waveVisualizerWidget);

        int sliderWidth = this.width / 3 - SCREEN_MARGIN_SIZE;

        new DirectionalLayoutBuilder(
            new DirectionalLayoutBuilder(
                new PreviewAnimationButtonWidget(),
                createCenteredCloseButton(ScreenTexts.BACK, this, 0, 0, sliderWidth - COMMON_BUTTON_SIZE * 4 - COMMON_SPACING * 4, COMMON_BUTTON_SIZE),
                PulsePreset.SUBTLY_SMALL.createButtonWidget(this),
                PulsePreset.MODERATELY_RUBBERY.createButtonWidget(this),
                PulsePreset.COMICALLY_LARGE.createButtonWidget(this)
            ).build(),
            NumberSliderOptionWidget.createBuilder(sliderWidth, Glissando.clientConfig.pulseMagnitude).build(),
            createMultilineTooltip(sliderWidth, MULTILINE_MAX_ROWS, false, Glissando.clientConfig.pulseMagnitude, Option::getSimpleDefaultText, this.textRenderer),

            NumberSliderOptionWidget.createBuilder(sliderWidth, Glissando.clientConfig.pulseBounces).build(),
            createMultilineTooltip(sliderWidth, MULTILINE_MAX_ROWS, false, Glissando.clientConfig.pulseBounces, Option::getSimpleDefaultText, this.textRenderer),

            NumberSliderOptionWidget.createBuilder(sliderWidth, Glissando.clientConfig.pulseAnimationTime).build(),
            createMultilineTooltip(sliderWidth, MULTILINE_MAX_ROWS, false, Glissando.clientConfig.pulseAnimationTime, Option::getSimpleDefaultText, this.textRenderer)
        )
            .vertical()
            .spacing(3)
            .position(
                widget -> waveVisualizerWidget.getX() + waveVisualizerWidget.getWidth() + SCREEN_MARGIN_SIZE,
                widget -> SCREEN_MARGIN_SIZE
            )
            .build()
            .forEachChild(this::addDrawableChild);
    }

    @Override
    public void tick() {
        this.ticksOpen++;
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

    private class PreviewAnimationButtonWidget extends ButtonWidget {

        protected PreviewAnimationButtonWidget() {
            super(0, 0, COMMON_BUTTON_SIZE, COMMON_BUTTON_SIZE, Text.translatable("glissando.gui.preview_animation"), widget -> PulseAnimationOptionsScreen.this.startPreview(), DEFAULT_NARRATION_SUPPLIER);
            this.setTooltip(Tooltip.of(this.getMessage()));
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {}

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            super.renderWidget(context, mouseX, mouseY, deltaTicks);

            int x = this.getX() + COMMON_BUTTON_SIZE / 2 - COMMON_TEXTURE_SIZE / 2;
            int y = this.getY() + COMMON_BUTTON_SIZE / 2 - COMMON_TEXTURE_SIZE / 2;

            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getTexture(), x, y, COMMON_TEXTURE_SIZE, COMMON_TEXTURE_SIZE, this.alpha);
        }

        private Identifier getTexture() {
            String path = "config/pulse_animation_visualizer/pulse_preview";

            if (PulseAnimationOptionsScreen.this.isPreviewing)
                path += "_restart";

            return Glissando.id(path);
        }

        @Override
        public void playDownSound(SoundManager soundManager) {
            soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_HARP, 1.0F));
        }

    }

    public enum PulsePreset implements SimpleEnumStringIdentifiable {

        SUBTLY_SMALL(0.1F, 0, 5),
        MODERATELY_RUBBERY(0.2F, 1, 8),
        COMICALLY_LARGE(0.8F, 3, 40);

        private final float magnitude;
        private final int bounces;
        private final int time;

        PulsePreset(float magnitude, int bounces, int time) {
            this.magnitude = magnitude;
            this.bounces = bounces;
            this.time = time;
        }

        public ButtonWidget createButtonWidget(PulseAnimationOptionsScreen screen) {
            Text text = Text.translatable("glissando.option.pulse_preset." + this.asString());
            Identifier texture = Glissando.id("config/pulse_animation_visualizer/pulse_preset_" + this.asString());

            return createSimpleButton(text, texture, widget -> {
                this.set();
                screen.resetPreviewState();
            }, true);
        }

        public void set() {
            Glissando.clientConfig.pulseMagnitude.set(this.magnitude);
            Glissando.clientConfig.pulseBounces.set(this.bounces);
            Glissando.clientConfig.pulseAnimationTime.set(this.time);
        }

    }

}
