package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.gui.widget.AbstractNoteKeyWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import static com.axialeaa.glissando.util.NoteBlockUtils.*;

public record NoteKeyTextureGroup(Identifier released, Identifier pressed, Identifier selected, Identifier pressedSelected, Identifier outline, Identifier outlineSelected, int width, int height, MouseOverWidgetFunction mouseOverPredicate) {

    public static final NoteKeyTextureGroup NATURAL = NoteKeyTextureGroup.create(
        "natural",
        NATURAL_KEY_WIDTH,
        NATURAL_KEY_HEIGHT,
        NoteBlockUtils::isMouseOverNatural
    );

    public static final NoteKeyTextureGroup NATURAL_LEFT = NoteKeyTextureGroup.create(
        "natural_left",
        NATURAL_KEY_WIDTH,
        KEYBOARD_HEIGHT,
        NoteBlockUtils::isMouseOverNaturalLeft
    );

    public static final NoteKeyTextureGroup NATURAL_RIGHT = NoteKeyTextureGroup.create(
        "natural_right",
        NATURAL_KEY_WIDTH,
        KEYBOARD_HEIGHT,
        NoteBlockUtils::isMouseOverNaturalRight
    );

    public static final NoteKeyTextureGroup ACCIDENTAL = NoteKeyTextureGroup.create(
        "accidental",
        ACCIDENTAL_KEY_WIDTH,
        ACCIDENTAL_KEY_HEIGHT,
        NoteBlockUtils::isMouseOverAccidental
    );

    public static NoteKeyTextureGroup create(String name, int width, int height, MouseOverWidgetFunction mouseOverPredicate) {
        String path = "textures/gui/sprites/note_block/%s.png";

        Identifier released = Glissando.id(path.formatted(name));
        Identifier pressed = Glissando.id(path.formatted(name + "_pressed"));

        Identifier selected = Glissando.id(path.formatted(name + "_selected"));
        Identifier pressedSelected = Glissando.id(path.formatted(name + "_pressed_selected"));

        Identifier outline = Glissando.id(path.formatted(name + "_outline"));
        Identifier outlineSelected = Glissando.id(path.formatted(name + "_outline_selected"));

        return new NoteKeyTextureGroup(released, pressed, selected, pressedSelected, outline, outlineSelected, width, height, mouseOverPredicate);
    }

    public void draw(DrawContext context, int x, int y, boolean pressed, boolean selected) {
        Identifier texture = this.getTexture(pressed, selected);
        context.drawTexture(texture, x, y, 0, 0, this.width, this.height, this.width, this.height);
    }

    public void drawWithColor(DrawContext context, int x, int y, boolean pressed, boolean selected, int color) {
        Identifier texture = this.getTexture(pressed, selected);

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);

        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

        //? if >=1.21 {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        //?} else {
        /*BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        *///?}

        float maxX = x + this.width;
        float maxY = y + this.height;

        renderVertex(bufferBuilder, matrix4f, x,    y,    0, 0, color);
        renderVertex(bufferBuilder, matrix4f, x,    maxY, 0, 1, color);
        renderVertex(bufferBuilder, matrix4f, maxX, maxY, 1, 1, color);
        renderVertex(bufferBuilder, matrix4f, maxX, y,    1, 0, color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        context.drawTexture(this.getOutlineTexture(selected), x, y, 1, 0, 0, this.width, this.height, this.width, this.height);
    }

    private static void renderVertex(BufferBuilder builder, Matrix4f matrix, float x, float y, float u, float v, int color) {
        builder.vertex(matrix, x, y, 0).texture(u, v).color(color) /*? if <1.21 >>*//*.next()*/ ;
    }

    public <T extends AbstractNoteKeyWidget> boolean isMouseOver(T widget, double mouseX, double mouseY) {
        return widget.active && widget.visible && this.mouseOverPredicate.isMouseOver(widget, mouseX, mouseY);
    }

    private Identifier getTexture(boolean pressed, boolean selected) {
        Identifier texture = selected ? this.selected : this.released;

        if (!pressed)
            return texture;

        return this.getPressedTexture(texture);
    }

    private Identifier getOutlineTexture(boolean selected) {
        return selected ? this.outlineSelected : this.outline;
    }

    private Identifier getPressedTexture(Identifier texture) {
        if (texture == this.released)
            return this.pressed;

        if (texture == this.selected)
            return this.pressedSelected;

        return texture;
    }

    @FunctionalInterface
    public interface MouseOverWidgetFunction {

        <T extends AbstractNoteKeyWidget> boolean isMouseOver(T widget, double mouseX, double mouseY);

    }

}
