package com.axialeaa.glissando.config;

import com.axialeaa.glissando.GlissandoSyncedConfig;

public interface ServerConfigHolder {

    void setConfig(GlissandoSyncedConfig config);

    GlissandoSyncedConfig getConfig();

}
