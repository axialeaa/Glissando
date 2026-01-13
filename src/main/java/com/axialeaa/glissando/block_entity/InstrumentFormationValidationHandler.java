package com.axialeaa.glissando.block_entity;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.data.registry.GlissandoNoteBlockInstruments;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public record InstrumentFormationValidationHandler(NoteBlockEntity blockEntity) {

    public RegistryKey<DataNoteBlockInstrument> findInstrument(ServerWorld world, BlockPos pos, BlockState state, Registry<DataNoteBlockInstrument> registry) {
        Map<RegistryKey<DataNoteBlockInstrument>, DataNoteBlockInstrument> simpleInstruments = Maps.newHashMap();
        RegistryKey<DataNoteBlockInstrument> registryKey;

        for (DataNoteBlockInstrument instrument : registry) {
            registryKey = registry.getKey(instrument).orElseThrow();

            if (registryKey.equals(GlissandoNoteBlockInstruments.HARP))
                continue;

            if (!instrument.isMultiBlock()) {
                simpleInstruments.put(registryKey, instrument);
                continue;
            }

            if (instrument.formation().validate(world, pos, state, this.blockEntity))
                return registryKey;
        }

        for (Map.Entry<RegistryKey<DataNoteBlockInstrument>, DataNoteBlockInstrument> entry : simpleInstruments.entrySet()) {
            if (entry.getValue().formation().validate(world, pos, state, this.blockEntity))
                return entry.getKey();
        }

        return GlissandoNoteBlockInstruments.HARP;
    }

    public RegistryKey<DataNoteBlockInstrument> findSimpleInstrument(ServerWorld world, BlockPos pos, BlockState state, Registry<DataNoteBlockInstrument> registry) {
        RegistryKey<DataNoteBlockInstrument> registryKey;

        for (DataNoteBlockInstrument instrument : registry) {
            if (instrument.isMultiBlock())
                continue;

            registryKey = registry.getKey(instrument).orElseThrow();

            if (!registryKey.equals(GlissandoNoteBlockInstruments.HARP) && instrument.formation().validate(world, pos, state, this.blockEntity))
                return registryKey;
        }

        return GlissandoNoteBlockInstruments.HARP;
    }

}
