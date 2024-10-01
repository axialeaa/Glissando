package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.widget.AbstractNoteKeyWidget;
import com.axialeaa.glissando.mixin.accessor.ScreenAccessor;
import com.axialeaa.glissando.util.Note;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.OptionalInt;

import net.minecraft.client.gui.widget. /*$ icon_button >>*/ TextIconButtonWidget ;

import static com.axialeaa.glissando.util.GlissandoUtils.*;

public abstract class AbstractNoteBlockScreen<T extends AbstractNoteKeyWidget> extends Screen {

    /**
     * An {@link OptionalInt} representing the pitch of the latest-focused widget, if one exists.
     */
    protected OptionalInt selectedPitch = OptionalInt.empty();

    /**
     * A list of note key widgets on this screen.
     */
    protected ObjectArrayList<T> widgets = new ObjectArrayList<>();

    protected AbstractNoteBlockScreen(Text title) {
        super(title);
    }

    /**
     * @param x The horizontal position.
     * @param y The vertical position.
     * @param pitch The pitch of the note. Used as a list index.
     * @return a new instance of {@link T} which will be added to the list of screen children.
     */
    protected abstract T createNewWidget(int x, int y, int pitch);

    /**
     * @return the screen to go to when clicking the config button.
     * @see AbstractNoteBlockScreen#addConfigButton()
     */
    protected abstract Screen getConfigScreen();

    @Override
    protected void init() {
        this.widgets.clear();
        this.addKeys();
        this.addDoneButton();
        this.addConfigButton();
    }

    /**
     * Adds the {@code Done} button at the bottom of the screen and shifts it around to make space for the config button, if necessary.
     */
    private void addDoneButton() {
        boolean configButton = GlissandoConfig.get().configButton || !Glissando.MOD_MENU_INSTALLED;
        int offset = GlissandoConfig.get().configButtonPosition.getDoneButtonOffset();

        this.addDrawableChild(ButtonWidget
            .builder(ScreenTexts.DONE, button -> this.close())
            .dimensions(
                this.width / 2 + (configButton ? offset : DEFAULT_DONE_BUTTON_OFFSET),
                this.height / 4 + BUTTON_HEIGHT,
                configButton ? OFFSET_DONE_BUTTON_WIDTH : DEFAULT_DONE_BUTTON_WIDTH,
                CONFIG_BUTTON_SIZE
            )
            .build()
        );
    }

    /**
     * Adds the config button at the bottom of the screen and moves it next to the {@code Done} button based on the config option.
     */
    private void addConfigButton() {
        if (!GlissandoConfig.get().configButton && Glissando.MOD_MENU_INSTALLED)
            return;

        Identifier texture = Glissando.id("note_block/config");
        Text name = Text.translatable("glissando.config.button");

        //? >1.20.1 {
        TextIconButtonWidget widget = TextIconButtonWidget.builder(name, button -> {
            if (this.client != null)
                this.client.setScreen(this.getConfigScreen());
            }, true).width(CONFIG_BUTTON_SIZE)
            .texture(
                texture,
                CONFIG_BUTTON_TEXTURE_SIZE,
                CONFIG_BUTTON_TEXTURE_SIZE
            )
            .build();
        //?} else {
        /*IconButtonWidget widget = IconButtonWidget.builder(name, texture, button -> {
            if (this.client != null)
                this.client.setScreen(this.getConfigScreen());
            })
            .iconSize(CONFIG_BUTTON_SIZE, CONFIG_BUTTON_SIZE)
            .textureSize(CONFIG_BUTTON_TEXTURE_SIZE, CONFIG_BUTTON_TEXTURE_SIZE)
            .uv(0, 0)
            .build();
        *///?}

        widget.setPosition(
            this.width / 2 + GlissandoConfig.get().configButtonPosition.getOffset(),
            this.height / 4 + BUTTON_HEIGHT
        );

        this.addDrawableChild(widget);
    }

    /**
     * Adds all note key widgets to the screen with appropriate offsets based on the {@link com.axialeaa.glissando.util.GlissandoUtils#NOTES list in GlissandoUtils}.
     */
    private void addKeys() {
        int keyboardStartX = this.width / 2 - KEYBOARD_WIDTH / 2;

        int naturalX = keyboardStartX;
        int accidentalX = keyboardStartX - SEMITONE_OFFSET;

        for (int pitch = 0; pitch < NOTES.length; pitch++) {
            Note note = getNote(pitch);

            if (note.accidental) {
                this.addKey(accidentalX, TALL_KEY_Y_POS, pitch);
                accidentalX += getOffsetForAccidental(note);

                continue;
            }

            this.addKey(naturalX, note.isTall() ? TALL_KEY_Y_POS : NATURAL_KEY_Y_POS, pitch); // moves tall keys up
            naturalX += NATURAL_KEY_WIDTH + KEY_PADDING;
        }
    }

    /**
     * @param note The accidental.
     * @return The horizontal offset by which to move the note key widget.
     */
    private static int getOffsetForAccidental(Note note) {
        int newX = ACCIDENTAL_KEY_WIDTH + KEY_PADDING;

        if (note == Note.F_SHARP || note == Note.G_SHARP)
            newX += KEY_PADDING;
        else if (note == Note.A_SHARP || note == Note.D_SHARP)
            newX += NATURAL_KEY_WIDTH + SEMITONE_OFFSET;

        return newX;
    }

    /**
     * Adds an instance of {@link T} to the list of screen children.
     * @param x The horizontal position.
     * @param y The vertical position.
     * @param pitch The pitch of the note. Used as a list index.
     */
    private void addKey(int x, int y, int pitch) {
        T widget = this.createNewWidget(x, y, pitch);

        this.widgets.add(pitch, widget);
        this.addDrawableChild(widget);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderGradientBackground(context);

        OptionalInt pitch = this.selectedPitch;

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            if (drawable instanceof AbstractNoteKeyWidget widget) {
                widget.render(context, mouseX, mouseY, pitch.isPresent() && this.getPitchFromWidget((T) widget) == pitch.getAsInt());
                continue;
            }

            drawable.render(context, mouseX, mouseY, delta);
        }

        int color = Colors.WHITE;

        if (GlissandoConfig.get().titleColors && pitch.isPresent())
            color = getArgbColor(pitch.getAsInt());

        DiffuseLighting.disableGuiDepthLighting();
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, color);
        DiffuseLighting.enableGuiDepthLighting();
    }

    /**
     * Renders the background of the screen, using colors as set by {@link GlissandoConfig}.
     * @param context The draw context instance.
     */
    private void renderGradientBackground(DrawContext context) {
        Color start = GlissandoConfig.get().backgroundStartColor;
        Color end = GlissandoConfig.get().backgroundEndColor;

        int startColor = ColorHelper.Argb.getArgb(start.getAlpha(), start.getRed(), start.getGreen(), start.getBlue());
        int endColor = ColorHelper.Argb.getArgb(end.getAlpha(), end.getRed(), end.getGreen(), end.getBlue());

        context.fillGradient(0, 0, this.width, this.height, startColor, endColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (T widget : this.widgets) {
            if (!widget.mouseClicked(mouseX, mouseY, button))
                continue;

            int pitch = this.getPitchFromWidget(widget);
            this.selectedPitch = OptionalInt.of(pitch);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int pitch = getPitch(keyCode);

        if (pitch >= 0) {
            this.selectedPitch = OptionalInt.of(pitch);
            this.getWidgetFromPitch(pitch).keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * @param widget The note key widget.
     * @return the pitch of the {@code widget}.
     */
    private int getPitchFromWidget(T widget) {
        return this.widgets.indexOf(widget);
    }

    /**
     * @param pitch The pitch of the widget.
     * @return the widget with a pitch equal to {@code pitch}.
     */
    private T getWidgetFromPitch(int pitch) {
        return this.widgets.get(pitch);
    }

}