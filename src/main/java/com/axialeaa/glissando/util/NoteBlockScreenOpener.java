package com.axialeaa.glissando.util;

import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Defines behaviour for a player entity capable of opening a {@link NoteBlockScreen} instance.
 */
public interface NoteBlockScreenOpener {

    void openScreen(NoteBlockScreen screen);

    ClientPlayerEntity getPlayer();

}
