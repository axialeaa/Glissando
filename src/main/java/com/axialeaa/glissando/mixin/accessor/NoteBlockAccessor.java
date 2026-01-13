package com.axialeaa.glissando.mixin.accessor;

import net.minecraft.block.NoteBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoteBlock.class)
public interface NoteBlockAccessor {

    @Invoker("getCustomSound")
    Identifier invokeGetCustomSound(World world, BlockPos pos);

}
