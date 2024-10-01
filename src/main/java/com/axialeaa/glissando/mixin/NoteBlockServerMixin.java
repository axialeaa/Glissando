package com.axialeaa.glissando.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//? if <=1.20.4
/*import net.minecraft.util.Hand;*/

@Mixin(NoteBlock.class)
public class NoteBlockServerMixin {

	@WrapMethod(method = "onUse")
	private ActionResult openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		//? if <=1.20.4
		/*Hand hand,*/
		BlockHitResult hit, Operation<ActionResult> original
	) {
		return ActionResult.CONSUME;
	}

}