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
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.enums./*$ instrument >>*/ NoteBlockInstrument ;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    private static final Object2ObjectArrayMap</*$ instrument >>*/ NoteBlockInstrument , List<RegistryKey<Block>>> INSTRUMENT_TO_BLOCKS_MAP = Util.make(new Object2ObjectArrayMap<>(), map -> {
        for (Block block : Registries.BLOCK) {
            BlockState blockState = block.getDefaultState();
            /*$ instrument >>*/ NoteBlockInstrument instrument = blockState.getInstrument();

            Optional<RegistryKey<Block>> optional = blockState.getRegistryEntry().getKey();

            if (optional.isEmpty())
                return;

            RegistryKey<Block> key = optional.get();

            if (map.containsKey(instrument))
                map.get(instrument).add(key);
            else if (instrument != /*$ instrument >>*/ NoteBlockInstrument .HARP)
                map.put(instrument, new ArrayList<>(List.of(key)));
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

            for (RegistryKey<Block> key : INSTRUMENT_TO_BLOCKS_MAP.get(instrument))
                builder.add(key);
        }
    }

}
