package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.util.GlissandoUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <=1.20.4
/*import net.minecraft.util.Hand;*/

@Mixin(NoteBlock.class)
public abstract class NoteBlockServerMixin {

	@Shadow protected abstract void playNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos);

	@WrapMethod(method = "onUse")
	private ActionResult openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		//? if <=1.20.4
		/*Hand hand,*/
		BlockHitResult hit, Operation<ActionResult> original
	) {
		if (!GlissandoUtils.isValidNoteBlock(state)) {
			this.playNote(player, state, world, pos);
			player.incrementStat(Stats.PLAY_NOTEBLOCK);
		}
		return ActionResult.CONSUME;
	}

}