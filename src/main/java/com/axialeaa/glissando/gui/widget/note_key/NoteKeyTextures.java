package com.axialeaa.glissando.gui.widget.note_key;

import com.axialeaa.glissando.Glissando;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

/**
 * @param released The identifier of the default texture.
 * @param pressed The identifier of the texture to use when the key is being pressed.
 * @param hovered The identifier of the texture to use when the key is being hovered over.
 * @param pressedHovered The identifier of the texture to use when the key is being hovered over and pressed.
 * @param colorable The identifier of the overlay texture to tint with the note pitch.
 * @param pressedColorable The identifier of the overlay texture to tint with the note pitch, when the key is being pressed.
 * @param width The width of the key textures.
 * @param height The height of the key textures.
 * @param hoverArea The boolean-returning function calculating whether a mouse position should register as being "over" the note key widget. This is important because {@link NoteKeyTextures#NATURAL_LEFT} and {@link NoteKeyTextures#NATURAL_RIGHT} are compound rectangles which, with the vanilla behaviour, would overlap with the adjacent accidental unless special padding is applied.
 */
public record NoteKeyTextures(Identifier released, Identifier pressed, Identifier hovered, Identifier pressedHovered, Identifier colorable, Identifier pressedColorable, int width, int height, WidgetHoverArea hoverArea) {

    public static final int NATURAL_WIDTH = 13;
    public static final int ACCIDENTAL_WIDTH = 11;
    public static final int NATURAL_HEIGHT = 32;
    public static final int ACCIDENTAL_HEIGHT = 32;
    public static final int TALL_KEY_HEIGHT = 65;

    public static final NoteKeyTextures NATURAL = create("natural", NATURAL_WIDTH, NATURAL_HEIGHT, WidgetHoverArea.NATURAL);
    public static final NoteKeyTextures NATURAL_LEFT = create("natural_left", NATURAL_WIDTH, TALL_KEY_HEIGHT, WidgetHoverArea.NATURAL_LEFT);
    public static final NoteKeyTextures NATURAL_RIGHT = create("natural_right", NATURAL_WIDTH, TALL_KEY_HEIGHT, WidgetHoverArea.NATURAL_RIGHT);
    public static final NoteKeyTextures ACCIDENTAL = create("accidental", ACCIDENTAL_WIDTH, ACCIDENTAL_HEIGHT, WidgetHoverArea.ACCIDENTAL);

    public static NoteKeyTextures create(String name, int width, int height, WidgetHoverArea hoverArea) {
        String path = "note_block/" + name + '/';

        Identifier released = Glissando.id(path + name);
        Identifier pressed = Glissando.id(path + name + "_pressed");

        Identifier hovered = Glissando.id(path + name + "_hovered");
        Identifier pressedHovered = Glissando.id(path + name + "_pressed_hovered");

        Identifier colorable = Glissando.id(path + name + "_colorable");
        Identifier pressedColorable = Glissando.id(path + name + "_pressed_colorable");

        return new NoteKeyTextures(released, pressed, hovered, pressedHovered, colorable, pressedColorable, width, height, hoverArea);
    }

    public void draw(DrawContext context, int x, int y, boolean pressed, boolean hovered) {
        this.drawTexture(context, this.get(pressed, hovered), x, y, Colors.WHITE);
    }

    public void drawColorable(DrawContext context, int x, int y, boolean pressed, int color) {
        this.drawTexture(context, this.getColorable(pressed), x, y, color);
    }

    private void drawTexture(DrawContext context, Identifier sprite, int x, int y, int color) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, sprite, x, y, this.width, this.height, color);
    }

    private Identifier get(boolean pressed, boolean hovered) {
        if (pressed)
            return hovered ? this.pressedHovered : this.pressed;

        return hovered ? this.hovered : this.released;
    }

    private Identifier getColorable(boolean pressed) {
        return pressed ? this.pressedColorable : this.colorable;
    }

}
