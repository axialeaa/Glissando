package com.axialeaa.glissando.data;

import com.axialeaa.glissando.Glissando;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class BlockTags {

    public static final TagKey<Block>
        BANJO_INSTRUMENT = ofInstrument("banjo"),
        BASEDRUM_INSTRUMENT = ofInstrument("basedrum"),
        BASS_INSTRUMENT = ofInstrument("bass"),
        BELL_INSTRUMENT = ofInstrument("bell"),
        BIT_INSTRUMENT = ofInstrument("bit"),
        CHIME_INSTRUMENT = ofInstrument("chime"),
        COW_BELL_INSTRUMENT = ofInstrument("cow_bell"),
        CREEPER_INSTRUMENT = ofInstrument("creeper"),
        CUSTOM_HEAD_INSTRUMENT = ofInstrument("custom_head"),
        DIDGERIDOO_INSTRUMENT = ofInstrument("didgeridoo"),
        DRAGON_INSTRUMENT = ofInstrument("dragon"),
        FLUTE_INSTRUMENT = ofInstrument("flute"),
        GUITAR_INSTRUMENT = ofInstrument("guitar"),
        HAT_INSTRUMENT = ofInstrument("hat"),
        IRON_XYLOPHONE_INSTRUMENT = ofInstrument("iron_xylophone"),
        PIGLIN_INSTRUMENT = ofInstrument("piglin"),
        PLING_INSTRUMENT = ofInstrument("pling"),
        SKELETON_INSTRUMENT = ofInstrument("skeleton"),
        SNARE_INSTRUMENT = ofInstrument("snare"),
        WITHER_SKELETON_INSTRUMENT = ofInstrument("wither_skeleton"),
        XYLOPHONE_INSTRUMENT = ofInstrument("xylophone"),
        ZOMBIE_INSTRUMENT = ofInstrument("zombie");

    private static TagKey<Block> ofInstrument(String path) {
        return of("note_block_instruments", path);
    }

    private static TagKey<Block> of(String prefix, String path) {
        return TagKey.of(RegistryKeys.BLOCK, Glissando.vanillaId(prefix + "/" + path));
    }

    public static void init() {}

}
