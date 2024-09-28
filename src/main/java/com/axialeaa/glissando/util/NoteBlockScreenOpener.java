package com.axialeaa.glissando.util;

import com.axialeaa.glissando.gui.NoteBlockScreen;
import net.minecraft.client.network.ClientPlayerEntity;

public interface NoteBlockScreenOpener {

    void openScreen(NoteBlockScreen screen);

    ClientPlayerEntity getPlayer();

}
