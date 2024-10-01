package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

import static com.axialeaa.glissando.util.GlissandoUtils.*;

/**
 *
 * @param released The identifier of the default texture.
 * @param pressed The identifier of the texture to use when the key is pressed.
 * @param outline The identifier of the default outline texture.
 * @param outlineHovered The identifier of the outline texture to use when the key is being hovered over.
 * @param width The width of the key textures.
 * @param height The height of the key textures.
 * @param mouseOverPredicate T
 */
public record NoteKeyTextures(Identifier released, Identifier pressed, Identifier outline, Identifier outlineHovered, int width, int height, MouseOverWidgetFunction mouseOverPredicate) {

    public static final NoteKeyTextures NATURAL = NoteKeyTextures.create(
        "natural",
        NATURAL_KEY_WIDTH,
        NATURAL_KEY_HEIGHT,
        (x, y, mouseX, mouseY) -> {
            int maxX = x + NATURAL_KEY_WIDTH;
            int maxY = y + NATURAL_KEY_HEIGHT;

            return isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        }
    );

    public static final NoteKeyTextures NATURAL_LEFT = NoteKeyTextures.create(
        "natural_left",
        NATURAL_KEY_WIDTH,
        KEYBOARD_HEIGHT,
        (x, y, mouseX, mouseY) -> {
            int maxX = x + NATURAL_KEY_WIDTH - SEMITONE_OFFSET;
            int maxY = y + TALL_KEY_HEIGHT_DIFF;

            return isMouseOverTallKeyBase(x, y, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        }
    );

    public static final NoteKeyTextures NATURAL_RIGHT = NoteKeyTextures.create(
        "natural_right",
        NATURAL_KEY_WIDTH,
        KEYBOARD_HEIGHT,
        (x, y, mouseX, mouseY) -> {
            int minX = x + SEMITONE_OFFSET;
            int maxX = x + NATURAL_KEY_WIDTH;
            int maxY = y + TALL_KEY_HEIGHT_DIFF;

            return isMouseOverTallKeyBase(x, y, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, minX, y, maxX, maxY);
        }
    );

    public static final NoteKeyTextures ACCIDENTAL = NoteKeyTextures.create(
        "accidental",
        ACCIDENTAL_KEY_WIDTH,
        ACCIDENTAL_KEY_HEIGHT,
        (x, y, mouseX, mouseY) -> {
            int maxX = x + ACCIDENTAL_KEY_WIDTH;
            int maxY = y + ACCIDENTAL_KEY_HEIGHT;

            return isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        }
    );

    public static NoteKeyTextures create(String name, int width, int height, MouseOverWidgetFunction mouseOverPredicate) {
        String path = "textures/gui/sprites/note_block/%s.png";

        Identifier released = Glissando.id(path.formatted(name));
        Identifier pressed = Glissando.id(path.formatted(name + "_pressed"));

        Identifier outline = Glissando.id(path.formatted(name + "_outline"));
        Identifier outlineHovered = Glissando.id(path.formatted(name + "_outline_hovered"));

        return new NoteKeyTextures(released, pressed, outline, outlineHovered, width, height, mouseOverPredicate);
    }

    public static boolean isMouseOverTallKeyBase(int x, int y, double mouseX, double mouseY) {
        return isCoordinateInsideRect(mouseX, mouseY, x, y + TALL_KEY_HEIGHT_DIFF, x + NATURAL_KEY_WIDTH, y + NATURAL_KEY_HEIGHT + TALL_KEY_HEIGHT_DIFF);
    }

    public void draw(DrawContext context, int x, int y, boolean pressed, boolean hovered) {
        context.drawTexture(this.get(pressed), x, y, 0, 0, this.width, this.height, this.width, this.height);
        this.drawOutline(context, x, y, hovered);
    }

    public void drawWithColor(DrawContext context, int x, int y, boolean pressed, boolean hovered, int color) {
        if (color == Colors.WHITE || ColorHelper.Argb.getAlpha(color) == 0) {
            this.draw(context, x, y, pressed, hovered);
            return;
        }

        RenderSystem.setShaderTexture(0, this.get(pressed));
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

        this.drawOutline(context, x, y, hovered);
    }

    private void drawOutline(DrawContext context, int x, int y, boolean hovered) {
        context.drawTexture(this.getOutline(hovered), x, y, 0, 0, this.width, this.height, this.width, this.height);
    }

    private static void renderVertex(BufferBuilder builder, Matrix4f matrix, float x, float y, float u, float v, int color) {
        builder.vertex(matrix, x, y, 0).texture(u, v).color(color) /*? if <1.21 >>*//*.next()*/ ;
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
        return this.mouseOverPredicate.isMouseOver(x, y, mouseX, mouseY);
    }

    private Identifier get(boolean pressed) {
        return pressed ? this.pressed : this.released;
    }

    private Identifier getOutline(boolean hovered) {
        return hovered ? this.outlineHovered : this.outline;
    }

    @FunctionalInterface
    public interface MouseOverWidgetFunction {

        boolean isMouseOver(int x, int y, double mouseX, double mouseY);

    }

}
