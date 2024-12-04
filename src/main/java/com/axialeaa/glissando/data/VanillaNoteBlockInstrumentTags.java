package com.axialeaa.glissando.data;

import com.axialeaa.glissando.Glissando;
import net.minecraft.registry.tag.TagKey;

public class VanillaNoteBlockInstrumentTags {

    public static final TagKey<SerializableNoteBlockInstrument> TOP = of("top");
    public static final TagKey<SerializableNoteBlockInstrument> UNTUNABLE = of("untunable");

    private static TagKey<SerializableNoteBlockInstrument> of(String path) {
        return TagKey.of(SerializableNoteBlockInstrument.REGISTRY_KEY, Glissando.vanillaId(path));
    }

    public static void init() {}

}
