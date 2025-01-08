package com.axialeaa.glissando.gui.screen;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.GlissandoConfigScreen;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.widget.NoteKeyWidget;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import com.axialeaa.glissando.packet.TuneNoteBlockC2SPayload;

/**
 * The "normal" note block screen opened when interacting with a note block.
 */
public class NoteBlockScreen extends AbstractNoteBlockScreen {

    private final ClientWorld world;
    private final BlockPos pos;

    public NoteBlockScreen(ClientWorld world, BlockPos pos, @NotNull SerializableNoteBlockInstrument instrument) {
        super("note_block_screen", instrument);

        this.world = world;
        this.pos = pos;
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, int pitch) {
        return new NoteKeyWidget(x, y, pitch, this, false, this.pos);
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

        if (!mode.isReclusive())
            return;

        BlockState blockState = this.world.getBlockState(this.pos);
        Optional<Integer> optional = blockState.getOrEmpty(NoteBlock.NOTE);

        if (this.selectedWidget == null || optional.isEmpty())
            return;

        int pitch = this.getSelectedWidgetPitch();

        if (optional.get() != pitch)
            TuneNoteBlockC2SPayload.sendNew(this.pos, pitch, mode == InteractionMode.RECLUSIVE);
    }

    @Override
    protected void init() {
        super.init();

        BlockState blockState = this.world.getBlockState(this.pos);
        Optional<Integer> pitch = blockState.getOrEmpty(NoteBlock.NOTE);

        this.selectedWidget = pitch.map(this::getWidgetFromPitch).orElseThrow();
    }

    @Override
    public void tick() {
        if (!this.canEdit())
            this.close();

        SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(this.world, this.pos);

        if (this.instrument == instrument)
            return;

        this.instrument = instrument;

        for (NoteKeyWidget widget : this.widgets)
            widget.updateTooltip();
    }

    /**
     * @return true if this note block screen has a valid note block to edit. Fails if it's too far away or not a note block anymore.
     */
    private boolean canEdit() {
        if (this.client == null || this.client.player == null)
            return false;

        return isPlayerWithinRange(this.pos, this.client.player) && SerializableNoteBlockInstrument.canOpenNoteBlockScreen(this.world, this.pos, this.instrument);
    }

    /**
     * @param pos The position of the note block.
     * @param player The client player.
     * @return true if the {@code player} is too far away from the note block to tune it.
     * @see NoteBlockScreen#canEdit()
     */
    private static boolean isPlayerWithinRange(BlockPos pos, ClientPlayerEntity player) {
        return
            //? if >=1.20.6 {
            player.canInteractWithBlockAt(pos, 4.0);
            //?} else
            /*player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0;*/
    }

}
