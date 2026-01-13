package com.axialeaa.glissando.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.class)
public class AbstractBlockImplMixin {

    @WrapMethod(method = "onBlockAdded")
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, oldState, notify);
    }

    @WrapMethod(method = "getRenderType")
    public BlockRenderType getRenderTypeImpl(BlockState state, Operation<BlockRenderType> original) {
        return original.call(state);
    }

}
