package com.axialeaa.glissando.registries;

import com.axialeaa.glissando.Glissando;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.StringIdentifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public record NoteBlockInstrument(Text description, int startOctave, RegistryEntry<SoundEvent> soundEvent, Type type, TagKey<Block> blocks) {

    public static final Codec<NoteBlockInstrument> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            TextCodecs.CODEC.fieldOf("description").forGetter(NoteBlockInstrument::description),
            Codec.INT.optionalFieldOf("start_octave", 3).forGetter(NoteBlockInstrument::startOctave),
            SoundEvent.ENTRY_CODEC.fieldOf("sound_event").forGetter(NoteBlockInstrument::soundEvent),
            Type.CODEC.optionalFieldOf("type", Type.BASE_BLOCK).forGetter(NoteBlockInstrument::type),
            TagKey.codec(RegistryKeys.BLOCK).fieldOf("blocks").forGetter(NoteBlockInstrument::blocks)
        )
        .apply(instance, NoteBlockInstrument::new)
    );

    public static final Codec<RegistryEntry<NoteBlockInstrument>> ENTRY_CODEC = RegistryElementCodec.of(GlissandoRegistries.NOTE_BLOCK_INSTRUMENT, CODEC);

    public boolean isBase() {
        return this.type == Type.BASE_BLOCK;
    }

    public boolean isCustom() {
        return this.type == Type.CUSTOM;
    }

    public enum Type implements StringIdentifiable {

        BASE_BLOCK("base_block"),
        MOB_HEAD("mob_head"),
        CUSTOM("custom");

        private final String name;
        private static final Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values);

        Type(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

    }

    public static class Manager implements SynchronousResourceReloader {

        public static final Object2ObjectArrayMap<Block, NoteBlockInstrument> ALL_INSTRUMENTS = new Object2ObjectArrayMap<>();
        public static final Object2ObjectArrayMap<Block, NoteBlockInstrument> TOP_INSTRUMENTS = new Object2ObjectArrayMap<>();

        @Override
        public void reload(ResourceManager manager) {
            ResourceFinder finder = ResourceFinder.json("note_block_instruments");

            finder.findResources(manager).forEach((id, resource) -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream())) ) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    NoteBlockInstrument instrument = NoteBlockInstrument.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

                    for (Block block : Registries.BLOCK.stream().filter(block -> block.getDefaultState().isIn(instrument.blocks())).toList()) {
                        ALL_INSTRUMENTS.put(block, instrument);

                        if (!instrument.isBase())
                            TOP_INSTRUMENTS.put(block, instrument);
                    }
                }
                catch (IOException e) {
                    Glissando.LOGGER.error("Oops! Failed to read pottable json file {}", id.toString(), e);
                }
            });
        }

    }

}