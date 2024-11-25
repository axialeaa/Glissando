package com.axialeaa.glissando.util;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
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

public class GlissandoUtils {

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

    public static final int BUTTON_HEIGHT = 144;

    public static final int CONFIG_BUTTON_SIZE = 20;
    //? >1.20.1
     public static final int CONFIG_BUTTON_TEXTURE_SIZE = 16;

    public static final int DEFAULT_DONE_BUTTON_WIDTH = 200;
    public static final int DEFAULT_DONE_BUTTON_OFFSET = -(DEFAULT_DONE_BUTTON_WIDTH / 2);

    public static final int OFFSET_DONE_BUTTON_WIDTH = DEFAULT_DONE_BUTTON_WIDTH - CONFIG_BUTTON_SIZE;

    public static final Note[] NOTES = new Note[]{
        Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP,
    };

    public static Note getNote(int pitch) {
        return NOTES[pitch];
    }

    /**
     * The method that is called by the tune note block packet to tune the note block to a specified pitch.
     * @param pos The block position of the note block.
     * @param player The player who sent the packet.
     * @param pitch The pitch to tune the note block to.
     * @param play Whether the note block should play a sound when it has been tuned.
     */
    public static void tuneToPitch(BlockPos pos, ServerPlayerEntity player, int pitch, boolean play) {
        ServerWorld world = player.getServerWorld();
        SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);

        if (!SerializableNoteBlockInstrument.canOpenNoteBlockScreen(world, pos, instrument))
            return;

        BlockState blockState = world.getBlockState(pos).with(NoteBlock.NOTE, pitch);
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);

        if (play)
            ((NoteBlockAccessor) blockState.getBlock()).invokePlayNote(player, blockState, world, pos);

        player.incrementStat(Stats.TUNE_NOTEBLOCK);
    }

    /**
     * @param pos The position of the note block.
     * @param player The client player.
     * @return true if the {@code player} is too far away from the note block to tune it.
     * @see NoteBlockScreen#canEdit()
     */
    public static boolean isPlayerTooFar(BlockPos pos, ClientPlayerEntity player) {
        return
            //? if >=1.20.6 {
            !player.canInteractWithBlockAt(pos, 4.0);
            //?} else
            /*player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) > 64.0;*/
    }

    /**
     * @param pitch The pitch used to determine the color.
     * @return the color associated with {@code pitch} in ARGB format.
     * @see GlissandoUtils#getRgbColor(int)
     */
    public static int getArgbColor(int pitch) {
        return getRgbColor(pitch) | 0xFF000000;
    }

    /**
     * @param pitch The pitch used to determine the color.
     * @return the color associated with {@code pitch} in RGB format.
     */
    public static int getRgbColor(int pitch) {
        float delta = pitch / 24.0F;

        int red = (int) (Math.max(0.0F, MathHelper.sin(delta * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int green = (int) (Math.max(0.0F, MathHelper.sin((delta + (1.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);
        int blue = (int) (Math.max(0.0F, MathHelper.sin((delta + (2.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255);

        return red << 16 | green << 8 | blue;
    }

}