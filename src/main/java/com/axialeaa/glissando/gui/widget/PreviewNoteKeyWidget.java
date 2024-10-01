package com.axialeaa.glissando.gui.widget;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

public class PreviewNoteKeyWidget extends AbstractNoteKeyWidget {

    public PreviewNoteKeyWidget(int x, int y, int pitch) {
        super(x, y, pitch);
    }

    @Override
    public /*$ instrument >>*/ NoteBlockInstrument getInstrument() {
        return /*$ instrument >>*/ NoteBlockInstrument .HARP;
    }

}
