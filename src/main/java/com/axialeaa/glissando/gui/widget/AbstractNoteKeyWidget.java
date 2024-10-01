package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.util.GlissandoUtils;
import com.axialeaa.glissando.util.Note;
import com.axialeaa.glissando.util.NoteKeyTextures;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

//? if !=1.20.4
import com.axialeaa.glissando.mixin.accessor.ClickableWidgetAccessor;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

public abstract class AbstractNoteKeyWidget extends ButtonWidget {

    private final Note note;
    private final int pitch;

    public AbstractNoteKeyWidget(int x, int y, int pitch) {
        this(x, y, pitch, button -> {});
    }

    public AbstractNoteKeyWidget(int x, int y, int pitch, PressAction action) {
        super(x, y, GlissandoUtils.getNote(pitch).getWidth(), GlissandoUtils.getNote(pitch).getHeight(), ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);

        this.note = GlissandoUtils.getNote(pitch);
        this.pitch = pitch;

        if (GlissandoConfig.canShowTooltips())
            this.setTooltip(this.getTooltip());
    }

    public abstract /*$ instrument >>*/ NoteBlockInstrument getInstrument();

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!GlissandoConfig.get().mouseInputs)
            return false;

        return this.active && this.visible && this.note.textures.isMouseOver(this.getX(), this.getY(), mouseX, mouseY);
    }

    /**
     * @return a {@link MutableText} object containing each line of the tooltip text for this widget, based on the {@link GlissandoConfig config}.
     */
    public MutableText getTooltipContents() {
        MutableText text = Text.empty();
        boolean newLine = false;

        if (GlissandoConfig.get().noteTooltips) {
            String localizedTooltip = Text.translatable("glissando.tooltip.note").getString();
            String localizedPitch = Text.translatable("note.%s".formatted(this.pitch)).getString();

            text.append(localizedTooltip.formatted(localizedPitch));

            newLine = true;
        }

        if (newLine)
            text.append(Text.literal("\n"));

        if (GlissandoConfig.get().pitchTooltips) {
            String localizedTooltip = Text.translatable("glissando.tooltip.pitch").getString();
            text.append(localizedTooltip.formatted(this.pitch));

            newLine = true;
        }

        if (!GlissandoConfig.get().keybindTooltips || !GlissandoConfig.get().keybindInputs)
            return text;

        if (newLine)
            text.append(Text.literal("\n"));

        InputUtil.Key inputKey = InputUtil.fromKeyCode(GlissandoUtils.getKeyCode(this.pitch), 0);

        String localizedTooltip = Text.translatable("glissando.tooltip.press_key").getString();
        String localizedInputKey = inputKey.getLocalizedText().getString();

        return text.append(localizedTooltip.formatted(localizedInputKey));
    }

    /**
     * @return a {@link Tooltip} object with the {@link AbstractNoteKeyWidget#getTooltipContents() tooltip contents}, colored if the config allows it.
     */
    public Tooltip getTooltip() {
        MutableText mutableText = this.getTooltipContents();

        if (GlissandoConfig.get().tooltipColors) {
            int color = GlissandoUtils.getRgbColor(this.pitch);
            mutableText = mutableText.fillStyle(Style.EMPTY.withColor(color));
        }

        return Tooltip.of(mutableText);
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY);
    }

    /**
     * Renders this widget using the draw methods present in {@link NoteKeyTextures}.
     * @param context The draw context instance.
     * @param mouseX The horizontal position of the mouse cursor.
     * @param mouseY The vertical position of the mouse cursor.
     * @param pressed Whether this widget should render as "pressed".
     */
    public void render(DrawContext context, int mouseX, int mouseY, boolean pressed) {
        if (!this.visible)
            return;

        this.hovered = this.isMouseOver(mouseX, mouseY);

        //? if >=1.20.6 {
        ((ClickableWidgetAccessor) this).getTooltipState().render(this.isHovered(), this.isFocused(), this.getNavigationFocus());
         //?} elif =1.20.4 {
        /*Tooltip tooltip = this.getTooltip();

        if (tooltip != null)
            tooltip.render(this.isHovered(), this.isFocused(), this.getNavigationFocus());
        *///?} else
        /*((ClickableWidgetAccessor) this).invokeApplyTooltip();*/

        int x = this.getX();
        int y = this.getY();

        int color = GlissandoUtils.getArgbColor(this.pitch);
        NoteKeyTextures textures = this.note.textures;

        GlissandoConfig.get().keyboardColorPredicate.draw(textures, context, x, y, pressed, this.isHovered(), color);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(this.getInstrument().getSound(), NoteBlock.getNotePitch(this.pitch)));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!GlissandoConfig.get().keybindInputs || keyCode != GlissandoUtils.getKeyCode(this.pitch) || !this.active || !this.visible)
            return false;

        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
        this.onPress();

        return true;
    }

}
