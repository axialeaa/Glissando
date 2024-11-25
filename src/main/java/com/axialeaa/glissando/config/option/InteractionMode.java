package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;

@SuppressWarnings("unused")
public enum InteractionMode implements GlissandoNameableEnum {

    SILENT, SOCIAL, RECLUSIVE;

    @Override
    public String getOptionName() {
        return GlissandoConfig.INTERACTION_MODE;
    }

    public boolean isReclusive() {
        return this != SOCIAL;
    }

}
