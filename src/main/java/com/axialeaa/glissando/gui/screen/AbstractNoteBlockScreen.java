package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.widget.NoteKeyWidget;
import com.axialeaa.glissando.mixin.accessor.ScreenAccessor;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.util.GlissandoConstants;
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
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import static com.axialeaa.glissando.util.GlissandoConstants.*;

import net.minecraft.client.gui.widget. /*$ button >>*/ TextIconButtonWidget ;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNoteBlockScreen extends Screen {

    /**
     * An {@link OptionalInt} representing the pitch of the latest-focused widget, if one exists.
     */
    @Nullable
    protected NoteKeyWidget selectedWidget = null;

    /**
     * A list of note key widgets on this screen.
     */
    protected final ObjectArrayList<NoteKeyWidget> widgets = new ObjectArrayList<>();

    private final String name;
    public @NotNull SerializableNoteBlockInstrument instrument;

    protected AbstractNoteBlockScreen(String name, @NotNull SerializableNoteBlockInstrument instrument) {
        super(Glissando.translate(name + ".title"));

        this.name = name;
        this.instrument = instrument;
    }

    /**
     * @param x The horizontal position.
     * @param y The vertical position.
     * @param pitch The pitch of the note. Used as a list index.
     * @return a new instance of {@link NoteKeyWidget} which will be added to the list of screen children.
     */
    protected abstract NoteKeyWidget createNewWidget(int x, int y, int pitch);

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

        if (GlissandoConfig.get().configButton || !Glissando.MOD_MENU_LOADED)
            this.addConfigButton();
    }

    /**
     * Adds the {@code Done} button at the bottom of the screen and shifts it around to make space for the config button, if necessary.
     */
    private void addDoneButton() {
        boolean configButton = GlissandoConfig.get().configButton || !Glissando.MOD_MENU_LOADED;
        int offset = GlissandoConfig.get().configButtonPosition.getDoneButtonOffset();

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
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
        Text name = Glissando.translate("config.button");
        Identifier texture = GlissandoConstants.CONFIG_BUTTON_TEXTURE;

        ButtonWidget.PressAction pressAction = button -> {
            if (this.client != null)
                this.client.setScreen(this.getConfigScreen());
        };

        int x = this.width / 2 + GlissandoConfig.get().configButtonPosition.getOffset();
        int y = this.height / 4 + BUTTON_HEIGHT;

        //? >1.20.1 {
        TextIconButtonWidget widget = TextIconButtonWidget.builder(name, pressAction, true)
            .width(CONFIG_BUTTON_SIZE)
            .texture(texture, CONFIG_BUTTON_TEXTURE_SIZE, CONFIG_BUTTON_TEXTURE_SIZE)
            .build();

        widget.setPosition(x, y);
        //?} else {
        /*TexturedButtonWidget widget = new TexturedButtonWidget(
            x,
            y,
            CONFIG_BUTTON_SIZE,
            CONFIG_BUTTON_SIZE,
            0,
            0,
            CONFIG_BUTTON_SIZE,
            texture,
            CONFIG_BUTTON_SIZE,
            CONFIG_BUTTON_SIZE * 2,
            pressAction,
            name
        );
        *///?}

        this.addDrawableChild(widget);
    }

    /**
     * Adds all note key widgets to the screen with appropriate offsets based on the {@link Note#KEYBOARD_NOTES list in GlissandoUtils}.
     */
    private void addKeys() {
        int keyboardStartX = this.width / 2 - KEYBOARD_WIDTH / 2;

        int naturalX = keyboardStartX;
        int accidentalX = keyboardStartX - SEMITONE_OFFSET;

        for (int pitch = 0; pitch < Note.KEYBOARD_NOTES.length; pitch++) {
            Note note = Note.byPitch(pitch);

            if (note.isAccidental()) {
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
     * Adds an instance of {@link NoteKeyWidget} to the list of screen children.
     * @param x The horizontal position.
     * @param y The vertical position.
     * @param pitch The pitch of the note. Used as a list index.
     */
    private void addKey(int x, int y, int pitch) {
        NoteKeyWidget widget = this.createNewWidget(x, y, pitch);

        this.widgets.add(pitch, widget);
        widget.setNavigationOrder(pitch);

        this.addDrawableChild(widget);
    }

    @Override
    public Text getTitle() {
        if (!GlissandoConfig.get().showInstrument)
            return this.title;

        Text instrument = this.instrument.description();
        Text title = Glissando.translate(this.name + ".title_instrument");

        return Text.of(title.getString().formatted(instrument.getString()));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //? if >=1.20.6 {
        if (GlissandoConfig.get().backgroundBlur)
            this.applyBlur( /*? if <=1.21.1 >>*/ /*delta*/ );
        //?}

        this.renderGradientBackground(context);
        int pitch = this.getSelectedWidgetPitch();

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            if (drawable instanceof NoteKeyWidget widget) {
                widget.render(context, mouseX, mouseY, this.getPitchFromWidget(widget) == pitch);
                continue;
            }

            drawable.render(context, mouseX, mouseY, delta);
        }

        int color = Colors.WHITE;

        if (GlissandoConfig.get().titleColors && this.selectedWidget != null)
            color = Note.getArgbColor(pitch);

        DiffuseLighting.disableGuiDepthLighting();

        context.drawCenteredTextWithShadow(this.textRenderer, this.getTitle(), this.width / 2, 40, color);
        DiffuseLighting.enableGuiDepthLighting();
    }

    /**
     * Renders the background of the screen, using colors as set by {@link GlissandoConfig}.
     * @param context The draw context instance.
     */
    private void renderGradientBackground(DrawContext context) {
        Color start = GlissandoConfig.get().backgroundStartColor;
        Color end = GlissandoConfig.get().backgroundEndColor;

        int startColor = ColorHelper /*? if <=1.21.1 >>*/ /*.Argb*/ .getArgb(start.getAlpha(), start.getRed(), start.getGreen(), start.getBlue());
        int endColor = ColorHelper /*? if <=1.21.1 >>*/ /*.Argb*/ .getArgb(end.getAlpha(), end.getRed(), end.getGreen(), end.getBlue());

        context.fillGradient(0, 0, this.width, this.height, startColor, endColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Predicate<NoteKeyWidget> predicate = widget -> widget.mouseClicked(mouseX, mouseY, button);
        return this.iterateAndSetSelected(predicate) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Predicate<NoteKeyWidget> predicate = widget -> widget.isSelected() && widget.keyPressed(keyCode, scanCode, modifiers);
        return this.iterateAndSetSelected(predicate) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        int pitch = GlissandoConfig.get().keyboardLayout.getPitch(chr);

        if (pitch == -1)
            return false;

        NoteKeyWidget widget = this.getWidgetFromPitch(pitch);

        if (!widget.charTyped(chr, modifiers))
            return false;

        this.setSelectedWidget(widget);

        return true;
    }

    private boolean iterateAndSetSelected(Predicate<NoteKeyWidget> predicate) {
        for (NoteKeyWidget widget : this.widgets) {
            if (predicate.test(widget)) {
                this.setSelectedWidget(widget);
                return true;
            }
        }

        return false;
    }

    private void setSelectedWidget(NoteKeyWidget widget) {
        this.selectedWidget = widget;
        this.setFocused(widget);
    }

    int getSelectedWidgetPitch() {
        return this.getPitchFromWidget(this.selectedWidget);
    }

    private int getPitchFromWidget(NoteKeyWidget widget) {
        return this.widgets.indexOf(widget);
    }

    NoteKeyWidget getWidgetFromPitch(int pitch) {
        return this.widgets.get(pitch);
    }

}