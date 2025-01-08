package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.screen.AbstractNoteBlockScreen;
import com.axialeaa.glissando.packet.TuneNoteBlockC2SPayload;
import com.axialeaa.glissando.util.WidgetHoverArea;
import com.axialeaa.glissando.util.Note;
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
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

//? if !=1.20.4
import com.axialeaa.glissando.mixin.accessor.ClickableWidgetAccessor;

public class NoteKeyWidget extends ButtonWidget {

    private final Note note;
    private final int pitch;
    private final AbstractNoteBlockScreen screen;
    private final boolean preview;

    private boolean updateTooltip;

    public NoteKeyWidget(int x, int y, int pitch, AbstractNoteBlockScreen screen, boolean preview, @Nullable BlockPos pos) {
        super(x, y, Note.byPitch(pitch).getWidth(), Note.byPitch(pitch).getHeight(), ScreenTexts.EMPTY, getPressAction(pos, preview, pitch), DEFAULT_NARRATION_SUPPLIER);

        this.note = Note.byPitch(pitch);
        this.pitch = pitch;
        this.screen = screen;
        this.preview = preview;

        this.updateTooltip();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        WidgetHoverArea hoverArea = this.note.textures.hoverArea();
        return this.active && this.visible && hoverArea.isHovering(this.getX(), this.getY(), mouseX, mouseY);
    }

    private static PressAction getPressAction(@Nullable BlockPos pos, boolean preview, int pitch) {
        return button -> {
            if (pos == null)
                return;

            if (!preview && !GlissandoConfig.get().interactionMode.isReclusive())
                TuneNoteBlockC2SPayload.sendNew(pos, pitch, true);
        };
    }

    public void updateTooltip() {
        this.updateTooltip = true;
    }

    //? if <1.21.4 {
    /*@Override
    public boolean clicked(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY);
    }
    *///?}

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

        int color = Note.getArgbColor(this.pitch);
        NoteKeyTextures textures = this.note.textures;

        GlissandoConfig.get().keyboardColorMode.draw(textures, context, x, y, pressed, this.isSelected(), color);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered() || this.isFocused() && MinecraftClient.getInstance().getNavigationType().isKeyboard();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.preview || GlissandoConfig.get().interactionMode.isReclusive()) {
            RegistryEntry<SoundEvent> sound = this.screen.instrument.soundEvent();
            soundManager.play(PositionedSoundInstance.master(sound, NoteBlock.getNotePitch(this.pitch)));
        }
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
