package com.axialeaa.glissando.data;

import com.axialeaa.glissando.Glissando;
import net.minecraft.registry.tag.TagKey;

public class NoteBlockInstrumentTags {

    public static final TagKey<SerializableNoteBlockInstrument> TOP_INSTRUMENTS = of("top_instruments");

    private static TagKey<SerializableNoteBlockInstrument> of(String path) {
        return TagKey.of(SerializableNoteBlockInstrument.REGISTRY_KEY, Glissando.vanillaId(path));
    }

    public static void init() {}

}
