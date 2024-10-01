package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.GlissandoNameableEnum;

@SuppressWarnings("unused")
public enum ConfigButtonPosition implements GlissandoNameableEnum {

    LEFT  (-100, -80),
    RIGHT (80, -100);

    private final int offset;
    private final int doneButtonOffset;

    ConfigButtonPosition(int offset, int doneButtonOffset) {
        this.offset = offset;
        this.doneButtonOffset = doneButtonOffset;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getDoneButtonOffset() {
        return this.doneButtonOffset;
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.CONFIG_BUTTON_POSITION;
    }

}
