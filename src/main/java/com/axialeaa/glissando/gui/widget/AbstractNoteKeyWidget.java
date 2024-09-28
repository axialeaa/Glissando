package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.util.NoteKey;
import com.axialeaa.glissando.util.NoteBlockUtils;
import com.axialeaa.glissando.util.NoteKeyTextureGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;

//? if =1.20.4 {
/*import net.minecraft.client.gui.tooltip.Tooltip;
*///?} else
import com.axialeaa.glissando.mixin.accessor.ClickableWidgetAccessor;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

@Environment(EnvType.CLIENT)
public abstract class AbstractNoteKeyWidget extends ButtonWidget {

    private final MinecraftClient client;
    private final NoteKey noteKey;
    public boolean pressed;

    public AbstractNoteKeyWidget(int x, int y, NoteKey noteKey, MinecraftClient client, PressAction action) {
        super(x, y, noteKey.getWidth(), noteKey.getHeight(), ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);
        this.client = client;
        this.noteKey = noteKey;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!GlissandoConfig.get().mouseInputs)
            return false;

        return this.noteKey.getTextureGroup().isMouseOver(this, mouseX, mouseY);
    }

    public NoteKey getNoteKey() {
        return this.noteKey;
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY);
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY) {
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

        NoteKeyTextureGroup textures = this.noteKey.getTextureGroup();
        int color = NoteBlockUtils.getColorFromPitchWithAlpha(this.noteKey.getPitch());

        GlissandoConfig.get().keyboardColorPredicate.draw(textures, context, x, y, this.pressed, this.isSelected(), color);
    }

    public abstract /*$ instrument >>*/ NoteBlockInstrument getInstrument();

    @Override
    public void onPress() {
        this.pressed = true;
        super.onPress();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(this.getInstrument().getSound(), NoteBlock.getNotePitch(this.noteKey.getPitch())));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!GlissandoConfig.get().keybindInputs || keyCode != this.noteKey.getKeyCode() || !this.active || !this.visible)
            return false;

        this.playDownSound(this.client.getSoundManager());
        this.onPress();

        return true;
    }

}
