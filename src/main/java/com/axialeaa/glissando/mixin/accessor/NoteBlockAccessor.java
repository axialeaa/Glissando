package com.axialeaa.glissando.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoteBlock.class)
public interface NoteBlockAccessor {

    @Invoker("playNote")
    void invokePlayNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos);

}
