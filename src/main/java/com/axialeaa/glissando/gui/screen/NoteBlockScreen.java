package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.GlissandoConfigScreen;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.gui.widget.NoteKeyWidget;
import com.axialeaa.glissando.util.GlissandoUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.OptionalInt;

import com.axialeaa.glissando.packet. /*$ payload >>*/ TuneNoteBlockC2SPayload ;
import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

/**
 * The "normal" note block screen opened when interacting with a note block.
 */
public class NoteBlockScreen extends AbstractNoteBlockScreen<NoteKeyWidget> {

    private final ClientWorld world;
    private final BlockPos pos;

    public NoteBlockScreen(ClientWorld world, BlockPos pos, /*$ instrument >>*/ NoteBlockInstrument instrument) {
        super("note_block_screen", pos, instrument);

        this.world = world;
        this.pos = pos;

        BlockState blockState = world.getBlockState(pos);
        Optional<Integer> pitch = blockState.getOrEmpty(NoteBlock.NOTE);

        this.selectedPitch = pitch.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch, BlockPos pos) {
        return new NoteKeyWidget(x, y, pitch, pos, this);
    }

    @Override
    protected Screen getConfigScreen() {
        return GlissandoConfigScreen.create(this);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        super.close();

        InteractionMode mode = GlissandoConfig.get().interactionMode;

        if (mode == InteractionMode.SOCIAL)
            return;

        BlockState blockState = this.world.getBlockState(this.pos);
        Optional<Integer> optional = blockState.getOrEmpty(NoteBlock.NOTE);

        OptionalInt pitch = this.selectedPitch;

        if (pitch.isEmpty())
            return;

        if (optional.isPresent() && optional.get() != pitch.getAsInt())
            /*$ payload >>*/ TuneNoteBlockC2SPayload .sendNew(this.pos, pitch.getAsInt(), mode == InteractionMode.RECLUSIVE);
    }

    @Override
    public void tick() {
        if (!this.canEdit())
            this.close();

        BlockState blockState = this.world.getBlockState(this.pos);
        /*$ instrument >>*/ NoteBlockInstrument instrument = GlissandoUtils.getInstrument(blockState).orElse(/*$ instrument >>*/ NoteBlockInstrument .HARP);

        if (this.instrument == instrument)
            return;

        this.instrument = instrument;

        for (NoteKeyWidget widget : this.widgets)
            widget.updateTooltip();
    }

    /**
     * @return true if this note block screen has a valid note block to edit. Fails if it's too far away or not a note block anymore.
     */
    public boolean canEdit() {
        if (this.client == null || this.client.player == null)
            return false;

        BlockState blockState = this.world.getBlockState(this.pos);

        return !GlissandoUtils.isPlayerTooFar(this.pos, this.client.player) && GlissandoUtils.isValidNoteBlock(blockState);
    }

}
