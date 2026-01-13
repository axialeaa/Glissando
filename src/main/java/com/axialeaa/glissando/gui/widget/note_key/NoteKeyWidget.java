package com.axialeaa.glissando.gui.widget.note_key;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.config.option.KeyboardColorCondition;
import com.axialeaa.glissando.gui.screen.KeyboardScreen;
import com.axialeaa.glissando.mixin.accessor.ClickableWidgetAccessor;
import com.axialeaa.glissando.payload.TuneNoteBlockC2SPayload;
import com.axialeaa.glissando.util.ColorUtils;
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

import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class NoteKeyWidget extends ButtonWidget {

    public final Note note;
    public final int pitch;
    private final KeyboardScreen screen;
    private final boolean preview;

    public NoteKeyWidget(int x, int y, int pitch, Note note, KeyboardScreen screen, boolean preview, @Nullable BlockPos pos) {
        super(x, y, note.textures.width(), note.textures.height(), ScreenTexts.EMPTY, getPressAction(pos, preview, pitch), DEFAULT_NARRATION_SUPPLIER);

        this.note = note;
        this.pitch = pitch;
        this.screen = screen;
        this.preview = preview;

        this.updateTooltip();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && this.note.textures.hoverArea().contains(this.getX(), this.getY(), MathHelper.floor(mouseX), MathHelper.floor(mouseY));
    }

    private static PressAction getPressAction(@Nullable BlockPos pos, boolean preview, int pitch) {
        return button -> {
            if (pos != null && !preview && Glissando.clientConfig.interactionMode.get() == InteractionMode.SOCIAL)
                TuneNoteBlockC2SPayload.send(pos, (short) pitch, true);
        };
    }

    @Override
    public int getNavigationOrder() {
        return this.pitch + 10;
    }

    public void updateTooltip() {
        Tooltip tooltip = NoteKeyWidgetTooltip.of(this.pitch, this.screen.getInstrument());

        if (tooltip != null)
            this.setTooltip(tooltip);
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

        ((ClickableWidgetAccessor) this).getTooltipState().render(context, mouseX, mouseY, this.isHovered(), this.isFocused(), this.getNavigationFocus());

        int x = this.getX();
        int y = this.getY();

        int color = ColorUtils.getArgbPitchColor(this.pitch);
        NoteKeyTextures textures = this.note.textures;

        textures.draw(context, x, y, pressed, this.isSelected());
        KeyboardColorCondition colorMode = Glissando.clientConfig.keyboardColorCondition.get();

        if (colorMode.canDrawColorable(pressed))
            textures.drawColorable(context, x, y, pressed, color);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered() || this.isFocused() && MinecraftClient.getInstance().getNavigationType().isKeyboard();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (!this.preview && Glissando.clientConfig.interactionMode.get() == InteractionMode.SOCIAL)
            return;

        RegistryEntry<SoundEvent> sound = this.screen.getInstrument().soundEvent();
        soundManager.play(PositionedSoundInstance.master(sound.value(), NoteBlock.getNotePitch(this.pitch), 1.0F));
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
