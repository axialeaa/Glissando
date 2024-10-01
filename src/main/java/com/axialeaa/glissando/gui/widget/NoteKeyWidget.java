package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.math.BlockPos;

import com.axialeaa.glissando.packet. /*$ payload >>*/ TuneNoteBlockC2SPayload ;
import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

public class NoteKeyWidget extends AbstractNoteKeyWidget {

    private final BlockPos pos;

    public NoteKeyWidget(int x, int y, int pitch, BlockPos pos) {
        super(x, y, pitch, button -> {
            if (!GlissandoConfig.get().interactionMode.isReclusive())
                /*$ payload >>*/ TuneNoteBlockC2SPayload .sendNew(pos, pitch, false);
        });

        this.pos = pos;
    }

    @Override
    public /*$ instrument >>*/ NoteBlockInstrument getInstrument() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.world == null)
            return /*$ instrument >>*/ NoteBlockInstrument .HARP;

        BlockState blockState = client.world.getBlockState(this.pos);

        if (!(blockState.getBlock() instanceof NoteBlock))
            return /*$ instrument >>*/ NoteBlockInstrument .HARP;

        return blockState.get(NoteBlock.INSTRUMENT);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (GlissandoConfig.get().interactionMode.isReclusive())
            super.playDownSound(soundManager);
    }

}
