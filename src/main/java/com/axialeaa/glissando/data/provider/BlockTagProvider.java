package com.axialeaa.glissando.data.provider;

import com.axialeaa.glissando.data.VanillaBlockTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.enums./*$ instrument >>*/ NoteBlockInstrument ;
import net.fabricmc.fabric.api.tag.convention. /*$ convention_tag_package_ver >>*/ v2 .ConventionalBlockTags;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    private static final Object2ObjectArrayMap</*$ instrument >>*/ NoteBlockInstrument , List<RegistryKey<Block>>> INSTRUMENT_TO_BLOCKS_MAP = Util.make(new Object2ObjectArrayMap<>(), map -> {
        for (Block block : Registries.BLOCK) {
            BlockState blockState = block.getDefaultState();
            /*$ instrument >>*/ NoteBlockInstrument instrument = blockState.getInstrument();

            if (instrument == /*$ instrument >>*/ NoteBlockInstrument .HARP)
                continue;

            Optional<RegistryKey<Block>> optional = blockState.getRegistryEntry().getKey();

            if (optional.isEmpty())
                continue;

            RegistryKey<Block> key = optional.get();

            map.compute(instrument, (instrument1, keys) -> {
                if (keys != null) {
                    keys.add(key);
                    return keys;
                }

                return new ArrayList<>(List.of(key));
            });
        }
    });

    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (/*$ instrument >>*/ NoteBlockInstrument instrument : /*$ instrument >>*/ NoteBlockInstrument .values()) {
            if (instrument == /*$ instrument >>*/ NoteBlockInstrument .HARP)
                continue;

            TagKey<Block> tag = VanillaBlockTags.ofInstrument(instrument.asString());
            FabricTagBuilder builder = getOrCreateTagBuilder(tag).setReplace(false);

            populate(builder, instrument);
        }
    }

    private static void populate(FabricTagBuilder builder,  /*$ instrument >>*/ NoteBlockInstrument instrument) {
        populateTags(builder, instrument);
        populateBlocks(builder, instrument);
    }

    private static List<TagKey<Block>> getTagsForInstrument(/*$ instrument >>*/ NoteBlockInstrument instrument) {
        return switch (instrument) {
            case BASEDRUM -> List.of(
                //? if >=1.21.4
                ConventionalBlockTags.STORAGE_BLOCKS_RESIN,
                //? if >=1.21.1 {
                ConventionalBlockTags.OBSIDIANS,
                ConventionalBlockTags.CONCRETES,
                ConventionalBlockTags.GLAZED_TERRACOTTAS,
                //?}
                //? if >=1.20.6 {
                ConventionalBlockTags.STONES,
                ConventionalBlockTags.COBBLESTONES,
                ConventionalBlockTags.STORAGE_BLOCKS_COAL,
                ConventionalBlockTags.STORAGE_BLOCKS_RAW_COPPER,
                ConventionalBlockTags.STORAGE_BLOCKS_RAW_GOLD,
                ConventionalBlockTags.STORAGE_BLOCKS_RAW_IRON,
                ConventionalBlockTags.STORAGE_BLOCKS_LAPIS,
                ConventionalBlockTags.ORES,
                //?}
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
                //? if >=1.20.4
                BlockTags.CONCRETE_POWDER,
                BlockTags.SAND
            );
            case HAT -> List.of(
                ConventionalBlockTags.GLASS_BLOCKS,
                ConventionalBlockTags.GLASS_PANES
            );
            case BASS -> List.of(
                //? if >=1.20.6
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
            case GUITAR -> List.of(
                BlockTags.WOOL
            );
            //? if >=1.20.6 {
            case BANJO -> List.of(
                ConventionalBlockTags.STORAGE_BLOCKS_WHEAT
            );
            case BELL -> List.of(
                ConventionalBlockTags.STORAGE_BLOCKS_GOLD
            );
            case BIT -> List.of(
                ConventionalBlockTags.STORAGE_BLOCKS_EMERALD
            );
            case IRON_XYLOPHONE -> List.of(
                ConventionalBlockTags.STORAGE_BLOCKS_IRON
            );
            case XYLOPHONE -> List.of(
                ConventionalBlockTags.STORAGE_BLOCKS_BONE_MEAL
            );
            //?}
            default -> List.of();
        };
    }

    private static void populateTags(FabricTagBuilder builder, /*$ instrument >>*/ NoteBlockInstrument instrument) {
        for (TagKey<Block> tag : getTagsForInstrument(instrument))
            builder.addOptionalTag(tag);
    }

    private static void populateBlocks(FabricTagBuilder builder,  /*$ instrument >>*/ NoteBlockInstrument instrument) {
        for (RegistryKey<Block> key : INSTRUMENT_TO_BLOCKS_MAP.get(instrument))
            builder.add(key);
    }

}
