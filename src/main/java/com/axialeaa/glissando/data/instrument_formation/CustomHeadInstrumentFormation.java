package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class CustomHeadInstrumentFormation implements InstrumentFormation {

    public static final MapCodec<CustomHeadInstrumentFormation> CODEC = MapCodec.unit(new CustomHeadInstrumentFormation());

    @Override
    public InstrumentFormationType<?> getType() {
        return InstrumentFormationType.CUSTOM_HEAD;
    }

    @Override
    public boolean validate(ServerWorld world, BlockPos noteBlockPos, BlockState noteBlockState, NoteBlockEntity noteBlockEntity) {
        return world.getBlockEntity(noteBlockPos.up()) instanceof SkullBlockEntity skullBlockEntity && skullBlockEntity.getNoteBlockSound() != null;
    }

}
