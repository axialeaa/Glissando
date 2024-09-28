package com.axialeaa.glissando.gui;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.gui.widget.NoteKeyWidget;
import com.axialeaa.glissando.packet.UpdateNoteBlockC2SPayload;
import com.axialeaa.glissando.util.NoteBlockUtils;
import com.axialeaa.glissando.util.NoteKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class NoteBlockScreen extends AbstractNoteBlockScreen<NoteKeyWidget> {

    private static final Text TITLE = Text.translatable("glissando.note_block_screen.title");

    private final ClientWorld world;
    public final BlockPos pos;
    private final HashSet<NoteKeyWidget> noteKeyWidgets = new HashSet<>();

    private OptionalInt currentSelectedPitch;

    public NoteBlockScreen(ClientWorld world, BlockPos pos) {
        super(TITLE);
        this.world = world;
        this.pos = pos;

        BlockState blockState = world.getBlockState(pos);
        int pitch = blockState.getOrEmpty(NoteBlock.NOTE).orElseThrow();

        this.currentSelectedPitch = OptionalInt.of(pitch);
    }

    @Override
    public OptionalInt getCurrentSelectedPitch() {
        return this.currentSelectedPitch;
    }

    @Override
    protected void setCurrentSelectedPitch(int pitch) {
        this.currentSelectedPitch = OptionalInt.of(pitch);
    }

    @Override
    protected NoteKeyWidget createNewWidget(int x, int y, NoteKey noteKey) {
        return new NoteKeyWidget(x, y, noteKey, this.pos, this.client, this.world);
    }

    @Override
    protected HashSet<NoteKeyWidget> getWidgets() {
        return this.noteKeyWidgets;
    }

    @Override
    public void close() {
        super.close();

        if (GlissandoConfig.get().interactionMode == InteractionMode.SOCIAL)
            return;

        BlockState blockState = this.world.getBlockState(this.pos);
        Optional<Integer> optional = blockState.getOrEmpty(NoteBlock.NOTE);

        OptionalInt pitch = this.currentSelectedPitch;

        if (pitch.isEmpty())
            return;

        if (optional.isPresent() && optional.get() != pitch.getAsInt())
            UpdateNoteBlockC2SPayload.sendNew(this.pos, pitch.getAsInt());
    }

    @Override
    public void tick() {
        if (!this.canEdit())
            this.close();
    }

    private boolean canEdit() {
        if (this.client == null || this.world == null)
            return false;

        ClientPlayerEntity player = this.client.player;

        if (player == null)
            return false;

        BlockState blockState = this.world.getBlockState(this.pos);

        if (!(blockState.getBlock() instanceof NoteBlock))
            return false;

        return !NoteBlockUtils.isPlayerTooFar(this.pos, player);
    }

}
