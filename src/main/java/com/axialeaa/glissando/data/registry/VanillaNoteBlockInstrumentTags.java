package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import net.minecraft.registry.tag.TagKey;

public class VanillaNoteBlockInstrumentTags {

    public static final TagKey<SerializableNoteBlockInstrument>
        TOP = of("top"),
        UNTUNABLE = of("untunable");

    private static TagKey<SerializableNoteBlockInstrument> of(String path) {
        return TagKey.of(SerializableNoteBlockInstrument.REGISTRY_KEY, Glissando.vanillaId(path));
    }

    public static void init() {}

}
