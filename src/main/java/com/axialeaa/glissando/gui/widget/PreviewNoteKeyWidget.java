package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.util.NoteKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

@Environment(EnvType.CLIENT)
public class PreviewNoteKeyWidget extends AbstractNoteKeyWidget {

    public PreviewNoteKeyWidget(int x, int y, NoteKey noteKey, MinecraftClient client) {
        super(x, y, noteKey, client, button -> {});
    }

    @Override
    public /*$ instrument >>*/ NoteBlockInstrument getInstrument() {
        return /*$ instrument >>*/ NoteBlockInstrument .HARP;
    }

}
