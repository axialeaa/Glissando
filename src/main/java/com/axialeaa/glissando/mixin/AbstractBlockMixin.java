package com.axialeaa.glissando.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @WrapMethod(method = "onStateReplaced")
    public void onStateReplacedImpl(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, Operation<Void> original) {
        original.call(state, world, pos, newState, moved);
    }

}
