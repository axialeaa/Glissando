package com.axialeaa.glissando.mixin.config;

import com.axialeaa.glissando.GlissandoSyncedConfig;
import com.axialeaa.glissando.config.ServerConfigHolder;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements ServerConfigHolder {

    @Unique
    private GlissandoSyncedConfig config;

    @Override
    public void setConfig(GlissandoSyncedConfig config) {
        this.config = config;
    }

    @Override
    public GlissandoSyncedConfig getConfig() {
        return this.config;
    }

}
