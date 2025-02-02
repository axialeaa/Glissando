package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.Glissando;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import net.minecraft.block.enums./*$ instrument >>*/ NoteBlockInstrument ;
import static net.minecraft.block.enums./*$ instrument >>*/ NoteBlockInstrument .*;

public class VanillaBlockTags {

    public static final TagKey<Block>
        BANJO_INSTRUMENT = ofInstrument(BANJO),
        BASEDRUM_INSTRUMENT = ofInstrument(BASEDRUM),
        BASS_INSTRUMENT = ofInstrument(BASS),
        BELL_INSTRUMENT = ofInstrument(BELL),
        BIT_INSTRUMENT = ofInstrument(BIT),
        CHIME_INSTRUMENT = ofInstrument(CHIME),
        COW_BELL_INSTRUMENT = ofInstrument(COW_BELL),
        CREEPER_INSTRUMENT = ofInstrument(CREEPER),
        CUSTOM_HEAD_INSTRUMENT = ofInstrument(CUSTOM_HEAD),
        DIDGERIDOO_INSTRUMENT = ofInstrument(DIDGERIDOO),
        DRAGON_INSTRUMENT = ofInstrument(DRAGON),
        FLUTE_INSTRUMENT = ofInstrument(FLUTE),
        GUITAR_INSTRUMENT = ofInstrument(GUITAR),
        HAT_INSTRUMENT = ofInstrument(HAT),
        IRON_XYLOPHONE_INSTRUMENT = ofInstrument(IRON_XYLOPHONE),
        PIGLIN_INSTRUMENT = ofInstrument(PIGLIN),
        PLING_INSTRUMENT = ofInstrument(PLING),
        SKELETON_INSTRUMENT = ofInstrument(SKELETON),
        SNARE_INSTRUMENT = ofInstrument(SNARE),
        WITHER_SKELETON_INSTRUMENT = ofInstrument(WITHER_SKELETON),
        XYLOPHONE_INSTRUMENT = ofInstrument(XYLOPHONE),
        ZOMBIE_INSTRUMENT = ofInstrument(ZOMBIE);

    public static TagKey<Block> ofInstrument(/*$ instrument >>*/ NoteBlockInstrument instrument) {
        return of("note_block_instruments", instrument.asString());
    }

    private static TagKey<Block> of(String prefix, String path) {
        return TagKey.of(RegistryKeys.BLOCK, Glissando.vanillaId(path).withPrefixedPath(prefix + "/"));
    }

    public static void init() {}

}
