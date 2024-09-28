package com.axialeaa.glissando.util;

import com.axialeaa.glissando.gui.widget.AbstractNoteKeyWidget;
import com.axialeaa.glissando.mixin.accessor.NoteBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class NoteBlockUtils {

    public static final int KEYBOARD_WIDTH = 196;
    public static final int KEYBOARD_HEIGHT = 65;

    public static final int NATURAL_KEY_WIDTH = 13;
    public static final int ACCIDENTAL_KEY_WIDTH = 11;

    public static final int NATURAL_KEY_HEIGHT = 32;
    public static final int ACCIDENTAL_KEY_HEIGHT = 32;

    public static final int KEY_PADDING = 1;

    public static final int SEMITONE_OFFSET = (ACCIDENTAL_KEY_WIDTH - KEY_PADDING) / 2;
    public static final int TALL_KEY_HEIGHT_DIFF = KEYBOARD_HEIGHT - NATURAL_KEY_HEIGHT;

    public static final int NATURAL_KEY_Y_POS = 120;
    public static final int TALL_KEY_Y_POS = NATURAL_KEY_Y_POS - TALL_KEY_HEIGHT_DIFF;

    public static void tuneToPitch(BlockPos pos, ServerPlayerEntity player, int pitch) {
        ServerWorld world = player.getServerWorld();
        BlockState blockState = world.getBlockState(pos);

        if (!(blockState.getBlock() instanceof NoteBlock noteBlock))
            return;

        blockState = blockState.with(NoteBlock.NOTE, pitch);
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);

        ((NoteBlockAccessor) noteBlock).invokePlayNote(player, blockState, world, pos);
        player.incrementStat(Stats.TUNE_NOTEBLOCK);
    }

    public static boolean isPlayerTooFar(BlockPos pos, ClientPlayerEntity player) {
        return
            //? if >=1.20.6 {
            !player.canInteractWithBlockAt(pos, 4.0);
            //?} else
            /*player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) > 64.0;*/
    }

    public static int getColorFromPitchWithAlpha(int pitch) {
        return getColorFromPitchWithAlpha(pitch, 255);
    }

    public static int getColorFromPitchWithAlpha(int pitch, int alpha) {
        return getColorFromPitch(pitch) | alpha << 24;
    }

    public static int getColorFromPitch(int pitch) {
        float delta = pitch / 24.0F;

        int red = (int) (255 * Math.max(0.0F, MathHelper.sin(delta * MathHelper.TAU) * 0.65F + 0.35F));
        int green = (int) (255 * Math.max(0.0F, MathHelper.sin((delta + (1.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F));
        int blue = (int) (255 * Math.max(0.0F, MathHelper.sin((delta + (2.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F));

        return red << 16 | green << 8 | blue;
    }

    public static <T extends AbstractNoteKeyWidget> boolean isMouseOverNatural(T widget, double mouseX, double mouseY) {
        int minX = widget.getX();
        int maxX = widget.getX() + NATURAL_KEY_WIDTH;
        int minY = widget.getY();
        int maxY = widget.getY() + NATURAL_KEY_HEIGHT;

        return isCoordinateInsideRect(mouseX, mouseY, minX, minY, maxX, maxY);
    }

    public static <T extends AbstractNoteKeyWidget> boolean isMouseOverTallBase(T widget, double mouseX, double mouseY) {
        int minX = widget.getX();
        int maxX = widget.getX() + NATURAL_KEY_WIDTH;
        int minY = widget.getY() + TALL_KEY_HEIGHT_DIFF;
        int maxY = widget.getY() + NATURAL_KEY_HEIGHT + TALL_KEY_HEIGHT_DIFF;

        return isCoordinateInsideRect(mouseX, mouseY, minX, minY, maxX, maxY);
    }

    public static <T extends AbstractNoteKeyWidget> boolean isMouseOverNaturalRight(T widget, double mouseX, double mouseY) {
        int minX = widget.getX() + SEMITONE_OFFSET;
        int maxX = widget.getX() + NATURAL_KEY_WIDTH;
        int minY = widget.getY();
        int maxY = widget.getY() + TALL_KEY_HEIGHT_DIFF;

        return isMouseOverTallBase(widget, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, minX, minY, maxX, maxY);
    }

    public static <T extends AbstractNoteKeyWidget> boolean isMouseOverNaturalLeft(T widget, double mouseX, double mouseY) {
        int minX = widget.getX();
        int maxX = widget.getX() + NATURAL_KEY_WIDTH - SEMITONE_OFFSET;
        int minY = widget.getY();
        int maxY = widget.getY() + TALL_KEY_HEIGHT_DIFF;

        return isMouseOverTallBase(widget, mouseX, mouseY) || isCoordinateInsideRect(mouseX, mouseY, minX, minY, maxX, maxY);
    }

    public static <T extends AbstractNoteKeyWidget> boolean isMouseOverAccidental(T widget, double mouseX, double mouseY) {
        int minX = widget.getX();
        int maxX = widget.getX() + ACCIDENTAL_KEY_WIDTH;
        int minY = widget.getY();
        int maxY = widget.getY() + ACCIDENTAL_KEY_HEIGHT;

        return isCoordinateInsideRect(mouseX, mouseY, minX, minY, maxX, maxY);
    }

    public static boolean isCoordinateInsideRect(double x, double y, int minX, int minY, int maxX, int maxY) {
        boolean withinWidth = x >= minX && x < maxX;
        boolean withinHeight = y >= minY && y < maxY;

        return withinWidth && withinHeight;
    }

}