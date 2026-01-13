package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface InstrumentFormation {

    Codec<InstrumentFormation> TYPE_CODEC = InstrumentFormationType.REGISTRY.getCodec().dispatch(InstrumentFormation::getType, InstrumentFormationType::codec);

    InstrumentFormationType<?> getType();

    boolean validate(ServerWorld world, BlockPos noteBlockPos, BlockState noteBlockState, NoteBlockEntity noteBlockEntity);

}
