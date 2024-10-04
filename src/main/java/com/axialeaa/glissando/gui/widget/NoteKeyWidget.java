package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.math.BlockPos;

import com.axialeaa.glissando.packet. /*$ payload >>*/ TuneNoteBlockC2SPayload ;

public class NoteKeyWidget extends AbstractNoteKeyWidget {

    public NoteKeyWidget(int x, int y, int pitch, BlockPos pos, NoteBlockScreen screen) {
        super(x, y, pitch, button -> {
            if (!GlissandoConfig.get().interactionMode.isReclusive())
                /*$ payload >>*/ TuneNoteBlockC2SPayload .sendNew(pos, pitch, true);
        }, screen);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (GlissandoConfig.get().interactionMode.isReclusive())
            super.playDownSound(soundManager);
    }

}
