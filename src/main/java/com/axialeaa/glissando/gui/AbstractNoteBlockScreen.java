package com.axialeaa.glissando.gui;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.widget.AbstractNoteKeyWidget;
import com.axialeaa.glissando.mixin.accessor.ScreenAccessor;
import com.axialeaa.glissando.util.NoteKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.Collection;
import java.util.OptionalInt;

import static com.axialeaa.glissando.util.NoteBlockUtils.*;

@Environment(EnvType.CLIENT)
public abstract class AbstractNoteBlockScreen<W extends AbstractNoteKeyWidget> extends Screen {

    private int keyboardStartX;

    protected AbstractNoteBlockScreen(Text title) {
        super(title);
    }

    public abstract OptionalInt getCurrentSelectedPitch();

    protected abstract void setCurrentSelectedPitch(int pitch);

    @Override
    protected void init() {
        keyboardStartX = this.width / 2 - KEYBOARD_WIDTH / 2;

        this.addDrawableChild(ButtonWidget
            .builder(ScreenTexts.DONE, button -> this.close())
            .dimensions(this.width / 2 - 100, this.height / 4 + 144, 200, 20)
            .build()
        );

        this.getWidgets().clear();

        this.createNaturalKeys();
        this.createAccidentalKeys();
    }

    private void createNaturalKeys() {
        int x = this.keyboardStartX;

        for (NoteKey noteKey : NoteKey.NATURALS) {
            this.addKey(x, noteKey.isTall() ? TALL_KEY_Y_POS : NATURAL_KEY_Y_POS, noteKey); // moves tall keys up
            x += NATURAL_KEY_WIDTH + 1;
        }
    }

    private void createAccidentalKeys() {
        int x = this.keyboardStartX - SEMITONE_OFFSET;

        for (NoteKey noteKey : NoteKey.ACCIDENTALS) {
            this.addKey(x, TALL_KEY_Y_POS, noteKey);

            x += ACCIDENTAL_KEY_WIDTH + KEY_PADDING;

            if (noteKey.getNote() == NoteKey.Note.F_SHARP || noteKey.getNote() == NoteKey.Note.G_SHARP)
                x += KEY_PADDING; // The extra padding is needed in order to space F♯, G♯ and A♯ 2 pixels apart.
            else if (noteKey.getNote() == NoteKey.Note.A_SHARP || noteKey.getNote() == NoteKey.Note.D_SHARP)
                x += NATURAL_KEY_WIDTH + SEMITONE_OFFSET;
        }
    }

    protected abstract W createNewWidget(int x, int y, NoteKey noteKey);

    private void addKey(int x, int y, NoteKey noteKey) {
        W widget = this.createNewWidget(x, y, noteKey);
        widget.pressed = this.getCurrentSelectedPitch().isPresent() && noteKey.getPitch() == this.getCurrentSelectedPitch().getAsInt();

        if (GlissandoConfig.get().keybindTooltips || GlissandoConfig.get().noteTooltips)
            widget.setTooltip(noteKey.getTooltip());

        this.getWidgets().add(widget);
        this.addDrawableChild(widget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderGradientBackground(context);

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            if (drawable instanceof AbstractNoteKeyWidget noteKeyWidget) {
                noteKeyWidget.renderWidget(context, mouseX, mouseY);
                continue;
            }

            drawable.render(context, mouseX, mouseY, delta);
        }

        DiffuseLighting.disableGuiDepthLighting();

        int color = Colors.WHITE;
        OptionalInt pitch = this.getCurrentSelectedPitch();

        if (GlissandoConfig.get().titleColors && pitch.isPresent())
            color = getColorFromPitchWithAlpha(pitch.getAsInt());

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, color);

        DiffuseLighting.enableGuiDepthLighting();
    }

    private void renderGradientBackground(DrawContext context) {
        Color start = GlissandoConfig.get().backgroundStartColor;
        Color end = GlissandoConfig.get().backgroundEndColor;

        int startColor = ColorHelper.Argb.getArgb(start.getAlpha(), start.getRed(), start.getGreen(), start.getBlue());
        int endColor = ColorHelper.Argb.getArgb(end.getAlpha(), end.getRed(), end.getGreen(), end.getBlue());

        context.fillGradient(0, 0, this.width, this.height, startColor, endColor);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    protected abstract Collection<W> getWidgets();

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.toUnaryInput(this.getWidgets(), mouseX, mouseY, button, W::mouseClicked);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.toUnaryInput(this.getWidgets(), keyCode, scanCode, modifiers, W::keyPressed);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private <T, U, V> void toUnaryInput(Collection<W> widgets, T t, U u, V v, NoteKeyWidgetToggleFunction<W, T, U, V> function) {
        for (W noteKeyWidget : widgets) {
            if (function.shouldToggle(noteKeyWidget, t, u, v)) {
                widgets.forEach(widget -> {
                    this.setCurrentSelectedPitch(noteKeyWidget.getNoteKey().getPitch());
                    widget.pressed = widget == noteKeyWidget;
                });

                break;
            }
        }
    }

    @FunctionalInterface
    public interface NoteKeyWidgetToggleFunction<W, T, U, V> {

        boolean shouldToggle(W widget, T t, U u, V v);

    }

}