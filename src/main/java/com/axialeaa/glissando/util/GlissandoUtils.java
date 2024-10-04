package com.axialeaa.glissando.util;

import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import com.axialeaa.glissando.mixin.accessor.NoteBlockAccessor;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;
import static net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument .*;

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
    public static final int CONFIG_BUTTON_TEXTURE_SIZE = 16;

    public static final int DEFAULT_DONE_BUTTON_WIDTH = 200;
    public static final int DEFAULT_DONE_BUTTON_OFFSET = -(DEFAULT_DONE_BUTTON_WIDTH / 2);

    public static final int OFFSET_DONE_BUTTON_WIDTH = DEFAULT_DONE_BUTTON_WIDTH - CONFIG_BUTTON_SIZE;

    private static final CharArrayList CHARS = CharArrayList.of(
        '1', 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', 'u', '8', 'i', '9', 'o', '0', 'p',
        'z', 's', 'x', 'd', 'c', 'v', 'g'
    );

    /**
     * Stores a map of instruments along with the octave of the first F♯. This is used in the note tooltips, since not all instruments start on F♯3.
     */
    private static final Object2IntArrayMap</*$ instrument >>*/ NoteBlockInstrument > INSTRUMENT_TO_START_OCTAVE = Util.make(new Object2IntArrayMap<>(), map -> {
        map.defaultReturnValue(3);
        map.put(BASEDRUM, 0);
        map.put(BASS, 1);
        map.put(DIDGERIDOO, 1);
        map.put(GUITAR, 2);
        map.put(FLUTE, 4);
        map.put(SNARE, 4);
        map.put(BELL, 5);
        map.put(CHIME, 5);
        map.put(HAT, 5);
        map.put(XYLOPHONE, 5);
    });

    public static final Note[] NOTES = new Note[]{
        Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP, Note.G, Note.G_SHARP, Note.A, Note.A_SHARP, Note.B,
        Note.C, Note.C_SHARP, Note.D, Note.D_SHARP, Note.E, Note.F, Note.F_SHARP,
    };

    public static char getChar(int pitch) {
        return CHARS.getChar(pitch);
    }

    public static int getPitch(char chr) {
        return CHARS.indexOf(chr);
    }

    public static Note getNote(int pitch) {
        return NOTES[pitch];
    }

    public static int getOctave(/*$ instrument >>*/ NoteBlockInstrument instrument, int pitch) {
        int startOctave = INSTRUMENT_TO_START_OCTAVE.getInt(instrument);

        if (pitch < 6) // First C
            return startOctave;

        return startOctave + (pitch >= 18 ? 2 : 1); // Second C
    }

    /**
     * The method that is called by the tune note block packet to tune the note block to a specified pitch.
     * @param pos The block position of the note block.
     * @param player The player who sent the packet.
     * @param pitch The pitch to tune the note block to.
     * @param playOnChange Whether the note block should play a sound when it has been tuned.
     */
    public static void tuneToPitch(BlockPos pos, ServerPlayerEntity player, int pitch, boolean playOnChange) {
        ServerWorld world = player.getServerWorld();

        BlockState blockState = world.getBlockState(pos);

        if (!isValidNoteBlock(blockState))
            return;

        blockState = blockState.with(NoteBlock.NOTE, pitch);
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);

        if (!playOnChange)
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

    public static Optional</*$ instrument >>*/ NoteBlockInstrument > getInstrument(BlockState state) {
        if (!(state.getBlock() instanceof NoteBlock))
            return Optional.empty();

        Optional</*$ instrument >>*/ NoteBlockInstrument > optional = state.getOrEmpty(NoteBlock.INSTRUMENT);

        return optional;
    }

    public static boolean isValidNoteBlock(BlockState state) {
        return getInstrument(state).filter(instrument -> !instrument.isNotBaseBlock()).isPresent();
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

        float red = Math.max(0.0F, MathHelper.sin(delta * MathHelper.TAU) * 0.65F + 0.35F) * 255;
        float green = Math.max(0.0F, MathHelper.sin((delta + (1.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255;
        float blue = Math.max(0.0F, MathHelper.sin((delta + (2.0F / 3.0F)) * MathHelper.TAU) * 0.65F + 0.35F) * 255;

        return (int) (red * 255) << 16 | (int) (green * 255) << 8 | (int) (blue * 255);
    }

    public static boolean isCoordinateInsideRect(double x, double y, int minX, int minY, int maxX, int maxY) {
        boolean withinWidth = x >= minX && x < maxX;
        boolean withinHeight = y >= minY && y < maxY;

        return withinWidth && withinHeight;
    }

}