package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import com.axialeaa.glissando.util.NoteBlockScreenOpener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements NoteBlockScreenOpener {

    @Shadow @Final protected MinecraftClient client;

    @Override
    public void openScreen(NoteBlockScreen screen) {
        this.client.setScreen(screen);
    }

}
