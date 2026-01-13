package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.data.instrument_formation.CustomHeadInstrumentFormation;
import com.axialeaa.glissando.data.instrument_formation.SimpleInstrumentFormation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.Block;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.registry.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

import java.util.concurrent.CompletableFuture;

public interface GlissandoNoteBlockInstruments {

    RegistryKey<DataNoteBlockInstrument> BANJO = keyOf("banjo");
    RegistryKey<DataNoteBlockInstrument> BASEDRUM = keyOf("basedrum");
    RegistryKey<DataNoteBlockInstrument> BASS = keyOf("bass");
    RegistryKey<DataNoteBlockInstrument> BELL = keyOf("bell");
    RegistryKey<DataNoteBlockInstrument> BIT = keyOf("bit");
    RegistryKey<DataNoteBlockInstrument> CHIME = keyOf("chime");
    RegistryKey<DataNoteBlockInstrument> COW_BELL = keyOf("cow_bell");
    RegistryKey<DataNoteBlockInstrument> CREEPER = keyOf("creeper");
    RegistryKey<DataNoteBlockInstrument> CUSTOM_HEAD = keyOf("custom_head");
    RegistryKey<DataNoteBlockInstrument> DIDGERIDOO = keyOf("didgeridoo");
    RegistryKey<DataNoteBlockInstrument> DRAGON = keyOf("dragon");
    RegistryKey<DataNoteBlockInstrument> FLUTE = keyOf("flute");
    RegistryKey<DataNoteBlockInstrument> GUITAR = keyOf("guitar");
    RegistryKey<DataNoteBlockInstrument> HARP = keyOf("harp");
    RegistryKey<DataNoteBlockInstrument> HAT = keyOf("hat");
    RegistryKey<DataNoteBlockInstrument> IRON_XYLOPHONE = keyOf("iron_xylophone");
    RegistryKey<DataNoteBlockInstrument> PIGLIN = keyOf("piglin");
    RegistryKey<DataNoteBlockInstrument> PLING = keyOf("pling");
    RegistryKey<DataNoteBlockInstrument> SKELETON = keyOf("skeleton");
    RegistryKey<DataNoteBlockInstrument> SNARE = keyOf("snare");
    RegistryKey<DataNoteBlockInstrument> WITHER_SKELETON = keyOf("wither_skeleton");
    RegistryKey<DataNoteBlockInstrument> XYLOPHONE = keyOf("xylophone");
    RegistryKey<DataNoteBlockInstrument> ZOMBIE = keyOf("zombie");

    static void bootstrap(Registerable<DataNoteBlockInstrument> registerable) {
        RegistryEntryLookup<Block> blockLookup = registerable.getRegistryLookup(RegistryKeys.BLOCK);

        registerable.register(BANJO, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.BANJO_INSTRUMENT).build())
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BANJO)
            .description(Text.translatable("minecraft.note_block_instrument.banjo"))
            .build()
        );
        registerable.register(BASEDRUM, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.BASEDRUM_INSTRUMENT).build())
                .build()
            )
            .octave(0)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM)
            .description(Text.translatable("minecraft.note_block_instrument.basedrum"))
            .build()
        );
        registerable.register(BASS, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.BASS_INSTRUMENT).build())
                .build()
            )
            .octave(1)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BASS)
            .description(Text.translatable("minecraft.note_block_instrument.bass"))
            .build()
        );
        registerable.register(BELL, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.BELL_INSTRUMENT).build())
                .build()
            )
            .octave(5)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BELL)
            .description(Text.translatable("minecraft.note_block_instrument.bell"))
            .build()
        );
        registerable.register(BIT, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.BIT_INSTRUMENT).build())
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BIT)
            .description(Text.translatable("minecraft.note_block_instrument.bit"))
            .build()
        );
        registerable.register(CHIME, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.CHIME_INSTRUMENT).build())
                .build()
            )
            .octave(5)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_CHIME)
            .description(Text.translatable("minecraft.note_block_instrument.chime"))
            .build()
        );
        registerable.register(COW_BELL, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.COW_BELL_INSTRUMENT).build())
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL)
            .description(Text.translatable("minecraft.note_block_instrument.cow_bell"))
            .build()
        );
        registerable.register(CREEPER, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.CREEPER_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_CREEPER)
            .build()
        );
        registerable.register(CUSTOM_HEAD, DataNoteBlockInstrument.createBuilder()
            .formation(new CustomHeadInstrumentFormation())
            .build()
        );
        registerable.register(DIDGERIDOO, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.DIDGERIDOO_INSTRUMENT).build())
                .build()
            )
            .octave(1)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO)
            .description(Text.translatable("minecraft.note_block_instrument.didgeridoo"))
            .build()
        );
        registerable.register(DRAGON, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.DRAGON_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON)
            .build()
        );
        registerable.register(FLUTE, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.FLUTE_INSTRUMENT).build())
                .build()
            )
            .octave(4)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_FLUTE)
            .description(Text.translatable("minecraft.note_block_instrument.flute"))
            .build()
        );
        registerable.register(GUITAR, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.GUITAR_INSTRUMENT).build())
                .build()
            )
            .octave(2)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_GUITAR)
            .description(Text.translatable("minecraft.note_block_instrument.guitar"))
            .build()
        );
        registerable.register(HARP, DataNoteBlockInstrument.createBuilder()
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HARP)
            .description(Text.translatable("minecraft.note_block_instrument.harp"))
            .build()
        );
        registerable.register(HAT, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.HAT_INSTRUMENT).build())
                .build()
            )
            .octave(5)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HAT)
            .description(Text.translatable("minecraft.note_block_instrument.hat"))
            .build()
        );
        registerable.register(IRON_XYLOPHONE, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.IRON_XYLOPHONE_INSTRUMENT).build())
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE)
            .description(Text.translatable("minecraft.note_block_instrument.iron_xylophone"))
            .build()
        );
        registerable.register(PIGLIN, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.PIGLIN_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_PIGLIN)
            .build()
        );
        registerable.register(PLING, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.PLING_INSTRUMENT).build())
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_PLING)
            .description(Text.translatable("minecraft.note_block_instrument.pling"))
            .build()
        );
        registerable.register(SKELETON, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.SKELETON_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_SKELETON)
            .build()
        );
        registerable.register(SNARE, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.SNARE_INSTRUMENT).build())
                .build()
            )
            .octave(4)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_SNARE)
            .description(Text.translatable("minecraft.note_block_instrument.snare"))
            .build()
        );
        registerable.register(WITHER_SKELETON, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.WITHER_SKELETON_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON)
            .build()
        );
        registerable.register(XYLOPHONE, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.XYLOPHONE_INSTRUMENT).build())
                .build()
            )
            .octave(5)
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE)
            .description(Text.translatable("minecraft.note_block_instrument.xylophone"))
            .build()
        );
        registerable.register(ZOMBIE, DataNoteBlockInstrument.createBuilder()
            .formation(SimpleInstrumentFormation.createBuilder()
                .predicate(BlockPredicate.Builder.create().tag(blockLookup, GlissandoBlockTags.ZOMBIE_INSTRUMENT).build())
                .direction(Direction.UP)
                .build()
            )
            .soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE)
            .build()
        );
    }

    private static RegistryKey<DataNoteBlockInstrument> keyOf(String path) {
        return RegistryUtils.keyOf(DataNoteBlockInstrument.REGISTRY_KEY, path);
    }

    static void loadClass() {}

    class Provider extends FabricDynamicRegistryProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            entries.addAll(registries.getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY));
        }

        @Override
        public String getName() {
            return "Note Block Instruments";
        }

    }

}
