package com.axialeaa.glissando.config.gui.screen.branch;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.config.gui.widget.DirectionalLayoutBuilder;
import com.axialeaa.glissando.config.option.KeyboardColorCondition;
import com.axialeaa.glissando.config.option.KeyboardLayout;
import com.axialeaa.glissando.gui.screen.KeyboardScreen;
import com.axialeaa.glissando.util.ColorUtils;
import com.axialeaa.glissando.util.Note;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class KeyboardScreenOptionsScreen extends KeyboardScreen {

    private static final int BLUR_BUTTON_WIDTH = 110;

    private final MainConfigurationScreen parentScreen;

    public KeyboardScreenOptionsScreen(MainConfigurationScreen parentScreen) {
        super(Text.translatable("glissando.screen.keyboard_screen_options.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch, Note note) {
        NoteKeyWidget noteKeyWidget = new NoteKeyWidget(x, y, pitch, note, this, true, null);
        // noteKeyWidget.active = false;

        return noteKeyWidget;
    }

    @Override
    public int getDefaultSelectedWidgetPitch() {
        return this.parentScreen.getSelectedWidgetPitch();
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(createCheckboxBoolean(this.width / 2 + this.textRenderer.getWidth(this.getTitle()) / 2 + 4, 40 - CheckboxWidget.getCheckboxSize(this.textRenderer) / 4, Glissando.clientConfig.titleColor, this.textRenderer));

        new DirectionalLayoutBuilder(
            createCyclingEnum(BLUR_BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.keyboardColorCondition, KeyboardColorCondition.values(), true),
            createCyclingEnum(BLUR_BUTTON_WIDTH, COMMON_BUTTON_SIZE, Glissando.clientConfig.keyboardLayout, KeyboardLayout.values(), false)
        ).position(w -> SCREEN_MARGIN_SIZE, h -> SCREEN_MARGIN_SIZE).build().forEachChild(this::addDrawableChild);

        this.addDrawableChild(createCenteredCloseButton(ScreenTexts.BACK, this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (NoteKeyWidget widget : this.noteKeyWidgets)
            this.addCharText(context, widget);
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(this.parentScreen);
    }

    private void addCharText(DrawContext context, NoteKeyWidget widget) {
        String character = Glissando.clientConfig.keyboardLayout.get().getFormattedChar(widget.pitch);

        int x = widget.getX() + widget.getWidth() / 2 - this.textRenderer.getWidth(character) / 2 + COMMON_SPACING;
        int y = widget.getY();

        Note note = widget.note;
        boolean accidental = note.isAccidental();

        y += accidental ? -(this.textRenderer.getWrappedLinesHeight(character, 1) + COMMON_SPACING) : widget.getHeight() + COMMON_SPACING * 2;
        boolean canColor = Glissando.clientConfig.keyboardColorCondition.get().canDrawColorable(this.isSelectedWidget(widget));

        context.drawTextWithShadow(this.textRenderer, Text.literal(character), x, y, canColor ? ColorUtils.getArgbPitchColor(widget.pitch) : Colors.WHITE);
    }

}
