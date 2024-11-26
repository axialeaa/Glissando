package com.axialeaa.glissando.util;

import com.axialeaa.glissando.Glissando;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

//? if >=1.21.3
 /*import net.minecraft.client.gl.ShaderProgramKeys;*/

import static com.axialeaa.glissando.util.GlissandoUtils.*;

/**
 *
 * @param released The identifier of the default texture.
 * @param pressed The identifier of the texture to use when the key is pressed.
 * @param outline The identifier of the default outline texture.
 * @param outlineHovered The identifier of the outline texture to use when the key is being hovered over.
 * @param width The width of the key textures.
 * @param height The height of the key textures.
 * @param mouseOverPredicate The boolean-returning function calculating whether a mouse position should register as being "over" the note key. This is important because {@link NoteKeyTextures#NATURAL_LEFT} and {@link NoteKeyTextures#NATURAL_RIGHT} are compound rectangles which, with the vanilla behaviour, would overlap with the adjacent accidental unless special padding is applied.
 */
public record NoteKeyTextures(Identifier released, Identifier pressed, Identifier outline, Identifier outlineHovered, int width, int height, MouseOverPredicate mouseOverPredicate) {

    public static final NoteKeyTextures NATURAL = NoteKeyTextures.create("natural", NATURAL_KEY_WIDTH, NATURAL_KEY_HEIGHT, MouseOverPredicate.NATURAL);
    public static final NoteKeyTextures NATURAL_LEFT = NoteKeyTextures.create("natural_left", NATURAL_KEY_WIDTH, KEYBOARD_HEIGHT, MouseOverPredicate.NATURAL_LEFT);
    public static final NoteKeyTextures NATURAL_RIGHT = NoteKeyTextures.create("natural_right", NATURAL_KEY_WIDTH, KEYBOARD_HEIGHT, MouseOverPredicate.NATURAL_RIGHT);
    public static final NoteKeyTextures ACCIDENTAL = NoteKeyTextures.create("accidental", ACCIDENTAL_KEY_WIDTH, ACCIDENTAL_KEY_HEIGHT, MouseOverPredicate.ACCIDENTAL);

    public static NoteKeyTextures create(String name, int width, int height, MouseOverPredicate mouseOverPredicate) {
        String path = "textures/gui/sprites/note_block/%s.png";

        Identifier released = Glissando.id(path.formatted(name));
        Identifier pressed = Glissando.id(path.formatted(name + "_pressed"));

        Identifier outline = Glissando.id(path.formatted(name + "_outline"));
        Identifier outlineHovered = Glissando.id(path.formatted(name + "_outline_hovered"));

        return new NoteKeyTextures(released, pressed, outline, outlineHovered, width, height, mouseOverPredicate);
    }

    public void draw(DrawContext context, int x, int y, boolean pressed, boolean hovered) {
        this.drawTexture(context, this.get(pressed), x, y);
        this.drawOutlineTexture(context, x, y, hovered);
    }

    public void drawWithColor(DrawContext context, int x, int y, boolean pressed, boolean hovered, int color) {
        if (color == Colors.WHITE || ColorHelper /*? if <=1.21.1 >>*/ .Argb .getAlpha(color) == 0) {
            this.draw(context, x, y, pressed, hovered);
            return;
        }

        RenderSystem.setShaderTexture(0, this.get(pressed));
        RenderSystem.setShader( /*$ shader_program >>*/ GameRenderer::getPositionTexColorProgram );

        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

        //? if >=1.21.1 {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        //?} else {
        /*BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        *///?}

        this.renderQuad(bufferBuilder, matrix4f, x, y, color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        this.drawOutlineTexture(context, x, y, hovered);
    }

    private void drawOutlineTexture(DrawContext context, int x, int y, boolean hovered) {
        this.drawTexture(context, this.getOutline(hovered), x, y);
    }

    private void drawTexture(DrawContext context, Identifier sprite, int x, int y) {
        context.drawTexture(/*? if >=1.21.3 >>*/ /*RenderLayer::getGuiTextured,*/ sprite, x, y, 0, 0, this.width, this.height, this.width, this.height);
    }

    private void renderQuad(BufferBuilder builder, Matrix4f matrix, float x, float y, int color) {
        float maxX = x + this.width;
        float maxY = y + this.height;

        renderVertex(builder, matrix, x,    y,    0, 0, color);
        renderVertex(builder, matrix, x,    maxY, 0, 1, color);
        renderVertex(builder, matrix, maxX, maxY, 1, 1, color);
        renderVertex(builder, matrix, maxX, y,    1, 0, color);
    }

    private static void renderVertex(BufferBuilder builder, Matrix4f matrix, float x, float y, float u, float v, int color) {
        builder.vertex(matrix, x, y, 0).texture(u, v).color(color) /*? if <1.21 >>*//*.next()*/ ;
    }

    private Identifier get(boolean pressed) {
        return pressed ? this.pressed : this.released;
    }

    private Identifier getOutline(boolean hovered) {
        return hovered ? this.outlineHovered : this.outline;
    }

    @FunctionalInterface
    public interface MouseOverPredicate {

        MouseOverPredicate NATURAL = (x, y, mouseX, mouseY) -> {
            int maxX = x + NATURAL_KEY_WIDTH;
            int maxY = y + NATURAL_KEY_HEIGHT;

            return isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        };

        MouseOverPredicate NATURAL_LEFT = (x, y, mouseX, mouseY) -> {
            int maxX = x + NATURAL_KEY_WIDTH - SEMITONE_OFFSET;
            int maxY = y + TALL_KEY_HEIGHT_DIFF;

            return isMouseOverTallKeyBase(x, y, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        };

        MouseOverPredicate NATURAL_RIGHT = (x, y, mouseX, mouseY) -> {
            int minX = x + SEMITONE_OFFSET;
            int maxX = x + NATURAL_KEY_WIDTH;
            int maxY = y + TALL_KEY_HEIGHT_DIFF;

            return isMouseOverTallKeyBase(x, y, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, minX, y, maxX, maxY);
        };

        MouseOverPredicate ACCIDENTAL = (x, y, mouseX, mouseY) -> {
            int maxX = x + ACCIDENTAL_KEY_WIDTH;
            int maxY = y + ACCIDENTAL_KEY_HEIGHT;

            return isCoordinateInsideRect(mouseX, mouseY, x, y, maxX, maxY);
        };

        boolean isMouseOver(int x, int y, double mouseX, double mouseY);

        static boolean isMouseOverTallKeyBase(int x, int y, double mouseX, double mouseY) {
            return isCoordinateInsideRect(mouseX, mouseY, x, y + TALL_KEY_HEIGHT_DIFF, x + NATURAL_KEY_WIDTH, y + NATURAL_KEY_HEIGHT + TALL_KEY_HEIGHT_DIFF);
        }

        static boolean isCoordinateInsideRect(double x, double y, int minX, int minY, int maxX, int maxY) {
            boolean withinWidth = x >= minX && x < maxX;
            boolean withinHeight = y >= minY && y < maxY;

            return withinWidth && withinHeight;
        }

    }

}
