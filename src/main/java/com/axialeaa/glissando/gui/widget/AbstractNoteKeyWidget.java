package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.screen.AbstractNoteBlockScreen;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvent;

//? if !=1.20.4
import com.axialeaa.glissando.mixin.accessor.ClickableWidgetAccessor;

public abstract class AbstractNoteKeyWidget extends ButtonWidget {

    private final Note note;
    private final int pitch;
    private final AbstractNoteBlockScreen<?> screen;
    private boolean updateTooltip;

    public AbstractNoteKeyWidget(int x, int y, int pitch, AbstractNoteBlockScreen<?> screen) {
        this(x, y, pitch, button -> {}, screen);
    }

    public AbstractNoteKeyWidget(int x, int y, int pitch, PressAction action, AbstractNoteBlockScreen<?> screen) {
        super(x, y, GlissandoUtils.getNote(pitch).getWidth(), GlissandoUtils.getNote(pitch).getHeight(), ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);

        this.note = GlissandoUtils.getNote(pitch);
        this.pitch = pitch;
        this.screen = screen;

        this.updateTooltip();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && this.note.textures.mouseOverPredicate().isMouseOver(this.getX(), this.getY(), mouseX, mouseY);
    }

    public void updateTooltip() {
        this.updateTooltip = true;
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

        if (this.updateTooltip) {
            this.updateTooltip = false;
            Tooltip tooltip = NoteKeyWidgetTooltip.of(this.pitch, this.screen.instrument);

            if (tooltip != null)
                this.setTooltip(tooltip);
        }

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

        GlissandoConfig.get().keyboardColorPredicate.draw(textures, context, x, y, pressed, this.isSelected(), color);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered() || this.isFocused() && MinecraftClient.getInstance().getNavigationType().isKeyboard();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        RegistryEntry<SoundEvent> sound = this.screen.instrument.soundEvent();
        soundManager.play(PositionedSoundInstance.master(sound, NoteBlock.getNotePitch(this.pitch)));
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.active || !this.visible)
            return false;

        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
        this.onPress();

        return true;
    }

}
