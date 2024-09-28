package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.NoteBlockScreen;
import com.axialeaa.glissando.util.NoteBlockScreenOpener;
import com.axialeaa.glissando.util.NoteBlockUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//? if =1.20.1
/*import net.minecraft.util.Hand;*/

@Mixin(NoteBlock.class)
public class NoteBlockMixin extends BlockMixin {

	@WrapMethod(method = "onUse")
	private ActionResult openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		//? if =1.20.1
		/*Hand hand,*/
		BlockHitResult hit, Operation<ActionResult> original
	) {
		if (GlissandoConfig.get().restoreVanilla)
			return original.call(state, world, pos, player, /*? if =1.20.1 >>*/ /*hand,*/ hit);

		ActionResult result = ActionResult.success(world.isClient());

		if (!GlissandoConfig.get().openScreenPredicate.canOpenScreen(world, pos, state) || !(world instanceof ClientWorld clientWorld))
			return result;

		if (!(player instanceof NoteBlockScreenOpener screenOpener) || NoteBlockUtils.isPlayerTooFar(pos, screenOpener.getPlayer()))
			return result;

		screenOpener.openScreen(new NoteBlockScreen(clientWorld, pos));
		return result;
	}

	@Override
	public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
		if (GlissandoConfig.get().restoreVanilla) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		if (!GlissandoConfig.get().openScreenPredicate.canOpenScreen(world, pos, state) || !(world instanceof ClientWorld clientWorld)) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		if (!(placer instanceof NoteBlockScreenOpener screenOpener) || !GlissandoConfig.get().openScreenOnPlaced) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		screenOpener.openScreen(new NoteBlockScreen(clientWorld, pos));
		super.onPlacedImpl(world, pos, state, placer, itemStack, original);
	}

}