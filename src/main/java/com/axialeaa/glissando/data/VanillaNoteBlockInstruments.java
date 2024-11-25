package com.axialeaa.glissando.data;

import com.axialeaa.glissando.Glissando;
import net.minecraft.registry.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class VanillaNoteBlockInstruments {
    
    public static final RegistryKey<SerializableNoteBlockInstrument>
        BANJO = of("banjo"),
        BASEDRUM = of("basedrum"),
        BASS = of("bass"),
        BELL = of("bell"),
        BIT = of("bit"),
        CHIME = of("chime"),
        COW_BELL = of("cow_bell"),
        CREEPER = of("creeper"),
        CUSTOM_HEAD = of("custom_head"),
        DIDGERIDOO = of("didgeridoo"),
        DRAGON = of("dragon"),
        FLUTE = of("flute"),
        GUITAR = of("guitar"),
        HARP = of("harp"),
        HAT = of("hat"),
        IRON_XYLOPHONE = of("iron_xylophone"),
        PIGLIN = of("piglin"),
        PLING = of("pling"),
        SKELETON = of("skeleton"),
        SNARE = of("snare"),
        WITHER_SKELETON = of("wither_skeleton"),
        XYLOPHONE = of("xylophone"),
        ZOMBIE = of("zombie");
    
    public static void bootstrap(Registerable<SerializableNoteBlockInstrument> registerable) {
        registerable.register(BANJO, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.BANJO_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BANJO)
            .setDescription(Text.translatable("minecraft.note_block_instrument.banjo"))
            .build()
        );
        registerable.register(BASEDRUM, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.BASEDRUM_INSTRUMENT)
            .setOctave(0)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM)
            .setDescription(Text.translatable("minecraft.note_block_instrument.basedrum"))
            .build()
        );
        registerable.register(BASS, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.BASS_INSTRUMENT)
            .setOctave(1)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BASS)
            .setDescription(Text.translatable("minecraft.note_block_instrument.bass"))
            .build()
        );
        registerable.register(BELL, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.BELL_INSTRUMENT)
            .setOctave(5)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BELL)
            .setDescription(Text.translatable("minecraft.note_block_instrument.bell"))
            .build()
        );
        registerable.register(BIT, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.BIT_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_BIT)
            .setDescription(Text.translatable("minecraft.note_block_instrument.bit"))
            .build()
        );
        registerable.register(CHIME, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.CHIME_INSTRUMENT)
            .setOctave(5)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_CHIME)
            .setDescription(Text.translatable("minecraft.note_block_instrument.chime"))
            .build()
        );
        registerable.register(COW_BELL, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.COW_BELL_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL)
            .setDescription(Text.translatable("minecraft.note_block_instrument.cow_bell"))
            .build()
        );
        registerable.register(CREEPER, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.CREEPER_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_CREEPER)
            .build()
        );
        registerable.register(CUSTOM_HEAD, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.CUSTOM_HEAD_INSTRUMENT)
            .setSoundEvent(SoundEvents.UI_BUTTON_CLICK)
            .build()
        );
        registerable.register(DIDGERIDOO, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.DIDGERIDOO_INSTRUMENT)
            .setOctave(1)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO)
            .setDescription(Text.translatable("minecraft.note_block_instrument.didgeridoo"))
            .build()
        );
        registerable.register(DRAGON, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.DRAGON_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON)
            .build()
        );
        registerable.register(FLUTE, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.FLUTE_INSTRUMENT)
            .setOctave(4)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_FLUTE)
            .setDescription(Text.translatable("minecraft.note_block_instrument.flute"))
            .build()
        );
        registerable.register(GUITAR, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.GUITAR_INSTRUMENT)
            .setOctave(2)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_GUITAR)
            .setDescription(Text.translatable("minecraft.note_block_instrument.guitar"))
            .build()
        );
        registerable.register(HARP, SerializableNoteBlockInstrument.createBuilder()
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HARP)
            .setDescription(Text.translatable("minecraft.note_block_instrument.harp"))
            .build()
        );
        registerable.register(HAT, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.HAT_INSTRUMENT)
            .setOctave(5)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HAT)
            .setDescription(Text.translatable("minecraft.note_block_instrument.hat"))
            .build()
        );
        registerable.register(IRON_XYLOPHONE, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.IRON_XYLOPHONE_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE)
            .setDescription(Text.translatable("minecraft.note_block_instrument.iron_xylophone"))
            .build()
        );
        registerable.register(PIGLIN, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.PIGLIN_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_PIGLIN)
            .build()
        );
        registerable.register(PLING, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.PLING_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_PLING)
            .setDescription(Text.translatable("minecraft.note_block_instrument.pling"))
            .build()
        );
        registerable.register(SKELETON, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.SKELETON_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_SKELETON)
            .build()
        );
        registerable.register(SNARE, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.SNARE_INSTRUMENT)
            .setOctave(4)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_SNARE)
            .setDescription(Text.translatable("minecraft.note_block_instrument.snare"))
            .build()
        );
        registerable.register(WITHER_SKELETON, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.WITHER_SKELETON_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON)
            .build()
        );
        registerable.register(XYLOPHONE, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.XYLOPHONE_INSTRUMENT)
            .setOctave(5)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE)
            .setDescription(Text.translatable("minecraft.note_block_instrument.xylophone"))
            .build()
        );
        registerable.register(ZOMBIE, SerializableNoteBlockInstrument.createBuilder()
            .setBlocks(registerable, BlockTags.ZOMBIE_INSTRUMENT)
            .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE)
            .build()
        );
    }

    private static RegistryKey<SerializableNoteBlockInstrument> of(String path) {
        return RegistryKey.of(SerializableNoteBlockInstrument.REGISTRY_KEY, Glissando.vanillaId(path));
    }

    public static void init() {}

}
