package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public record SimpleInstrumentFormation(BlockPredicate predicate, Direction direction) implements InstrumentFormation {

    private static final BlockPredicate DEFAULT_PREDICATE = BlockPredicate.Builder.create().build();
    private static final Direction DEFAULT_DIRECTION = Direction.DOWN;

    public static final MapCodec<SimpleInstrumentFormation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            BlockPredicate.CODEC.optionalFieldOf("predicate", DEFAULT_PREDICATE).forGetter(SimpleInstrumentFormation::predicate),
            Direction.VERTICAL_CODEC.optionalFieldOf("direction", DEFAULT_DIRECTION).forGetter(SimpleInstrumentFormation::direction)
        )
        .apply(instance, SimpleInstrumentFormation::new)
    );

    @Override
    public InstrumentFormationType<?> getType() {
        return InstrumentFormationType.SIMPLE;
    }

    @Override
    public boolean validate(ServerWorld world, BlockPos noteBlockPos, BlockState noteBlockState, NoteBlockEntity noteBlockEntity) {
        if (this.direction == Direction.DOWN && !world.getBlockState(noteBlockPos.up()).isAir())
            return false;

        return this.predicate.test(world, noteBlockPos.offset(this.direction));
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private BlockPredicate predicate = DEFAULT_PREDICATE;
        private Direction direction = DEFAULT_DIRECTION;

        public Builder predicate(BlockPredicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public SimpleInstrumentFormation build() {
            return new SimpleInstrumentFormation(this.predicate, this.direction);
        }

    }

}
