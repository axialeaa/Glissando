package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.option.InteractionMode;
import com.axialeaa.glissando.packet.UpdateNoteBlockC2SPayload;
import com.axialeaa.glissando.util.NoteKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

@Environment(EnvType.CLIENT)
public class NoteKeyWidget extends AbstractNoteKeyWidget {

    private final ClientWorld world;
    private final BlockPos pos;

    public NoteKeyWidget(int x, int y, NoteKey noteKey, BlockPos pos, MinecraftClient client, ClientWorld world) {
        super(x, y, noteKey, client, button -> {
            if (GlissandoConfig.get().interactionMode == InteractionMode.RECLUSIVE)
                return;

            int pitch = noteKey.getPitch();
            UpdateNoteBlockC2SPayload.sendNew(pos, pitch);
        });

        this.world = world;
        this.pos = pos;
    }

    @Override
    public /*$ instrument >>*/ NoteBlockInstrument getInstrument() {
        BlockState blockState = this.world.getBlockState(this.pos);

        if (!(blockState.getBlock() instanceof NoteBlock))
            return /*$ instrument >>*/ NoteBlockInstrument .HARP;

        return blockState.get(NoteBlock.INSTRUMENT);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (GlissandoConfig.get().interactionMode == InteractionMode.RECLUSIVE)
            super.playDownSound(soundManager);
    }

}
