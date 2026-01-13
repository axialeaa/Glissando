package com.axialeaa.glissando;

import com.axialeaa.glissando.config.AbstractConfig;
import com.axialeaa.glissando.config.option.NumberSliderOption;

public class GlissandoSyncedConfig extends AbstractConfig {

    public NumberSliderOption.I multiBlockInstrumentCheckDelay = this.ofInt("multi_block_instrument_check_delay", 80, 1, 1200, true);
    public NumberSliderOption.I configPermissionRequirement = this.ofInt("config_permission_requirement", 2, 0, 4, true);

}
