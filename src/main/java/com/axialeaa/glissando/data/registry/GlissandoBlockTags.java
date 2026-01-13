package com.axialeaa.glissando.data.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.block.enums.NoteBlockInstrument.*;

public interface GlissandoBlockTags {

    TagKey<Block> BANJO_INSTRUMENT = ofInstrument(BANJO);
    TagKey<Block> BASEDRUM_INSTRUMENT = ofInstrument(BASEDRUM);
    TagKey<Block> BASS_INSTRUMENT = ofInstrument(BASS);
    TagKey<Block> BELL_INSTRUMENT = ofInstrument(BELL);
    TagKey<Block> BIT_INSTRUMENT = ofInstrument(BIT);
    TagKey<Block> CHIME_INSTRUMENT = ofInstrument(CHIME);
    TagKey<Block> COW_BELL_INSTRUMENT = ofInstrument(COW_BELL);
    TagKey<Block> CREEPER_INSTRUMENT = ofInstrument(CREEPER);
    TagKey<Block> DIDGERIDOO_INSTRUMENT = ofInstrument(DIDGERIDOO);
    TagKey<Block> DRAGON_INSTRUMENT = ofInstrument(DRAGON);
    TagKey<Block> FLUTE_INSTRUMENT = ofInstrument(FLUTE);
    TagKey<Block> GUITAR_INSTRUMENT = ofInstrument(GUITAR);
    TagKey<Block> HAT_INSTRUMENT = ofInstrument(HAT);
    TagKey<Block> IRON_XYLOPHONE_INSTRUMENT = ofInstrument(IRON_XYLOPHONE);
    TagKey<Block> PIGLIN_INSTRUMENT = ofInstrument(PIGLIN);
    TagKey<Block> PLING_INSTRUMENT = ofInstrument(PLING);
    TagKey<Block> SKELETON_INSTRUMENT = ofInstrument(SKELETON);
    TagKey<Block> SNARE_INSTRUMENT = ofInstrument(SNARE);
    TagKey<Block> WITHER_SKELETON_INSTRUMENT = ofInstrument(WITHER_SKELETON);
    TagKey<Block> XYLOPHONE_INSTRUMENT = ofInstrument(XYLOPHONE);
    TagKey<Block> ZOMBIE_INSTRUMENT = ofInstrument(ZOMBIE);

    static TagKey<Block> ofInstrument(NoteBlockInstrument instrument) {
        return RegistryUtils.tagOf(RegistryKeys.BLOCK, "note_block_instruments/" + instrument.asString());
    }

    static void loadClass() {}

    class Provider extends FabricTagProvider.BlockTagProvider {

        private static final Object2ObjectArrayMap<NoteBlockInstrument, List<Block>> INSTRUMENT_TO_BLOCKS_MAP = Util.make(new Object2ObjectArrayMap<>(), map -> {
            for (Block block : Registries.BLOCK) {
                BlockState blockState = block.getDefaultState();
                NoteBlockInstrument instrument = blockState.getInstrument();

                if (instrument == NoteBlockInstrument.HARP)
                    continue;

                map.compute(instrument, (instrument1, blocks) -> {
                    if (blocks == null)
                        return new ArrayList<>(List.of(block));

                    blocks.add(block);

                    return blocks;
                });
            }
        });

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            for (NoteBlockInstrument instrument : NoteBlockInstrument.values()) {
                if (instrument == NoteBlockInstrument.HARP)
                    continue;

                TagKey<Block> tag = ofInstrument(instrument);
                ProvidedTagBuilder<Block, Block> builder = this.valueLookupBuilder(tag);

                populate(builder, instrument);
            }
        }

        private static void populate(ProvidedTagBuilder<Block, Block> builder, NoteBlockInstrument instrument) {
            populateTagReferences(builder, instrument);
            populateDirectReferences(builder, instrument);
        }

        private static List<TagKey<Block>> getTagsForInstrument(NoteBlockInstrument instrument) {
            return switch (instrument) {
                case BASEDRUM -> List.of(
                    ConventionalBlockTags.STORAGE_BLOCKS_RESIN,
                    ConventionalBlockTags.OBSIDIANS,
                    ConventionalBlockTags.CONCRETES,
                    ConventionalBlockTags.GLAZED_TERRACOTTAS,
                    ConventionalBlockTags.STONES,
                    ConventionalBlockTags.COBBLESTONES,
                    ConventionalBlockTags.STORAGE_BLOCKS_COAL,
                    ConventionalBlockTags.STORAGE_BLOCKS_RAW_COPPER,
                    ConventionalBlockTags.STORAGE_BLOCKS_RAW_GOLD,
                    ConventionalBlockTags.STORAGE_BLOCKS_RAW_IRON,
                    ConventionalBlockTags.STORAGE_BLOCKS_LAPIS,
                    ConventionalBlockTags.ORES,
                    ConventionalBlockTags.SANDSTONE_BLOCKS,
                    ConventionalBlockTags.SANDSTONE_SLABS,
                    ConventionalBlockTags.SANDSTONE_STAIRS,
                    BlockTags.STONE_BRICKS,
                    BlockTags.CORAL_BLOCKS,
                    BlockTags.CORALS,
                    BlockTags.WALL_CORALS,
                    BlockTags.TERRACOTTA,
                    BlockTags.NYLIUM,
                    BlockTags.BASE_STONE_OVERWORLD,
                    BlockTags.BASE_STONE_NETHER,
                    BlockTags.STONE_PRESSURE_PLATES
                );
                case SNARE -> List.of(
                    BlockTags.CONCRETE_POWDER,
                    BlockTags.SAND
                );
                case HAT -> List.of(
                    ConventionalBlockTags.GLASS_BLOCKS,
                    ConventionalBlockTags.GLASS_PANES
                );
                case BASS -> List.of(
                    ConventionalBlockTags.WOODEN_CHESTS,
                    ConventionalBlockTags.WOODEN_BARRELS,
                    BlockTags.LOGS,
                    BlockTags.PLANKS,
                    BlockTags.WOODEN_DOORS,
                    BlockTags.WOODEN_FENCES,
                    BlockTags.WOODEN_STAIRS,
                    BlockTags.WOODEN_SLABS,
                    BlockTags.WOODEN_PRESSURE_PLATES,
                    BlockTags.WOODEN_TRAPDOORS,
                    BlockTags.BEEHIVES,
                    BlockTags.BANNERS,
                    BlockTags.CAMPFIRES
                );
                case GUITAR -> List.of(BlockTags.WOOL);
                case BANJO -> List.of(ConventionalBlockTags.STORAGE_BLOCKS_WHEAT);
                case BELL -> List.of(ConventionalBlockTags.STORAGE_BLOCKS_GOLD);
                case BIT -> List.of(ConventionalBlockTags.STORAGE_BLOCKS_EMERALD);
                case IRON_XYLOPHONE -> List.of(ConventionalBlockTags.STORAGE_BLOCKS_IRON);
                case XYLOPHONE -> List.of(ConventionalBlockTags.STORAGE_BLOCKS_BONE_MEAL);
                default -> List.of();
            };
        }

        private static void populateTagReferences(ProvidedTagBuilder<Block, Block> builder, NoteBlockInstrument instrument) {
            for (TagKey<Block> tag : getTagsForInstrument(instrument))
                builder.addOptionalTag(tag);
        }

        private static void populateDirectReferences(ProvidedTagBuilder<Block, Block> builder, NoteBlockInstrument instrument) {
            for (Block block : INSTRUMENT_TO_BLOCKS_MAP.get(instrument))
                builder.add(block);
        }

    }

}
