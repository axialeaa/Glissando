package com.axialeaa.glissando.data.instrument_formation;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public record MultiBlockInstrumentFormation(List<Pair<Vec3i, BlockPredicate>> multiBlock, AcceptedSymmetries acceptedSymmetries) implements InstrumentFormation {

    private static final List<Pair<Vec3i, BlockPredicate>> DEFAULT_MULTI_BLOCK = List.of();
    private static final AcceptedSymmetries DEFAULT_ACCEPTED_SYMMETRIES = AcceptedSymmetries.ROTATIONS_AND_FLIPS;

    public static final int MAX_RANGE = 8;

    public static final MapCodec<MultiBlockInstrumentFormation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            Codec.mapPair(Vec3i.CODEC.fieldOf("stack"), BlockPredicate.CODEC.fieldOf("predicate"))
                .codec()
                .listOf()
                .optionalFieldOf("multi_block", DEFAULT_MULTI_BLOCK)
                .validate(MultiBlockInstrumentFormation::getValidatedDataResult)
                .forGetter(MultiBlockInstrumentFormation::multiBlock),
            AcceptedSymmetries.CODEC
                .optionalFieldOf("accepted_symmetries", DEFAULT_ACCEPTED_SYMMETRIES)
                .forGetter(MultiBlockInstrumentFormation::acceptedSymmetries)
        )
        .apply(instance, MultiBlockInstrumentFormation::new)
    );

    private static DataResult<List<Pair<Vec3i, BlockPredicate>>> getValidatedDataResult(List<Pair<Vec3i, BlockPredicate>> multiBlock) {
        for (Pair<Vec3i, BlockPredicate> pair : multiBlock) {
            Vec3i vec3i = pair.getFirst();

            if (vec3i == Vec3i.ZERO)
                return DataResult.error(() -> "Invalid position: [0, 0, 0] is reserved for the note block");

            if (!isPositionInMultiBlockRange(vec3i.getX(), vec3i.getY(), vec3i.getZ()))
                return DataResult.error(() -> "Invalid position: [%s] is too far away from [0, 0, 0], absolute value of coordinate must not exceed %d".formatted(vec3i, MAX_RANGE));
        }

        return DataResult.success(multiBlock);
    }

    private static boolean isPositionInMultiBlockRange(double x, double y, double z) {
        return Math.abs(x) <= MAX_RANGE && Math.abs(y) <= MAX_RANGE && Math.abs(z) <= MAX_RANGE;
    }

    @Override
    public InstrumentFormationType<?> getType() {
        return InstrumentFormationType.MULTI_BLOCK;
    }

    @Override
    public boolean validate(ServerWorld world, BlockPos noteBlockPos, BlockState noteBlockState, NoteBlockEntity noteBlockEntity) {
        if (this.multiBlock.isEmpty())
            return false;

        if (this.acceptedSymmetries == AcceptedSymmetries.STRICT)
            return this.checkBlocks(world, noteBlockPos, BlockRotation.NONE, false);

        for (BlockRotation rotation : BlockRotation.values()) {
            if (this.checkBlocks(world, noteBlockPos, rotation, false))
                return true;

            if (this.acceptedSymmetries == AcceptedSymmetries.ROTATIONS_AND_FLIPS && this.checkBlocks(world, noteBlockPos, rotation, true))
                return true;
        }

        return false;
    }

    private boolean checkBlocks(ServerWorld world, BlockPos noteBlockPos, BlockRotation rotation, boolean flip) {
        BlockMirror mirror = flip ? BlockMirror.FRONT_BACK : BlockMirror.NONE;

        for (Pair<Vec3i, BlockPredicate> pair : this.multiBlock) {
            Vec3i vec3i = this.transformVector(pair.getFirst(), rotation, mirror);
            BlockPredicate predicate = pair.getSecond();

            if (!testTransformed(world, noteBlockPos.add(vec3i), predicate, rotation, mirror))
                return false;
        }

        return true;
    }

    private static boolean testTransformed(ServerWorld world, BlockPos pos, BlockPredicate predicate, BlockRotation rotation, BlockMirror mirror) {
        if (!world.isPosLoaded(pos))
            return false;

        BlockState blockState = world.getBlockState(pos).rotate(rotation).mirror(mirror);

        if (!predicate.testState(blockState))
            return false;

        if (predicate.nbt().isEmpty() && predicate.components().isEmpty())
            return true;

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (predicate.nbt().isPresent() && !BlockPredicate.testNbt(world, blockEntity, predicate.nbt().get()))
            return false;

        return predicate.components().isEmpty() || BlockPredicate.testComponents(blockEntity, predicate.components());
    }

    private Vec3i transformVector(Vec3i vec3i, BlockRotation rotation, BlockMirror mirror) {
        Vector3f vector = toVector3f(vec3i);

        vector = rotation.getDirectionTransformation().getMatrix().transform(vector);
        vector = mirror.getDirectionTransformation().getMatrix().transform(vector);

        return toVec3i(vector);
    }

    private static Vector3f toVector3f(Vec3i vec3i) {
        return new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    private static Vec3i toVec3i(Vector3f vector3f) {
        return new Vec3i((int) vector3f.x, (int) vector3f.y, (int) vector3f.z);
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private final List<Pair<Vec3i, BlockPredicate>> multiBlock = new ArrayList<>(DEFAULT_MULTI_BLOCK);
        private AcceptedSymmetries acceptedSymmetries = DEFAULT_ACCEPTED_SYMMETRIES;

        public Builder entry(int x, int y, int z, BlockPredicate predicate) {
            return this.entry(new Vec3i(x, y, z), predicate);
        }

        public Builder entry(Vec3i vec3i, BlockPredicate predicate) {
            this.multiBlock.add(new Pair<>(vec3i, predicate));
            return this;
        }

        public Builder entries(BlockPredicate predicate, Vec3i... vec3is) {
            for (Vec3i vec3i : vec3is)
                this.entry(vec3i, predicate);

            return this;
        }

        public Builder acceptedSymmetries(AcceptedSymmetries acceptedSymmetries) {
            this.acceptedSymmetries = acceptedSymmetries;
            return this;
        }

        public MultiBlockInstrumentFormation build() {
            return new MultiBlockInstrumentFormation(this.multiBlock, this.acceptedSymmetries);
        }

    }

}
