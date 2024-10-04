package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import com.axialeaa.glissando.util.NoteBlockScreenOpener;
import com.axialeaa.glissando.util.GlissandoUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//? if <=1.20.4
/*import net.minecraft.util.Hand;*/

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

@Mixin(NoteBlock.class)
public class NoteBlockClientMixin extends BlockMixin {

	@WrapMethod(method = "onUse")
	private ActionResult openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		//? if <=1.20.4
		/*Hand hand,*/
		BlockHitResult hit, Operation<ActionResult> original
	) {
		if (!world.isClient() || !GlissandoUtils.isValidNoteBlock(state))
			return ActionResult.CONSUME;

		if (!(world instanceof ClientWorld clientWorld) || GlissandoUtils.isPlayerTooFar(pos, (ClientPlayerEntity) player))
			return ActionResult.SUCCESS;

		/*$ instrument >>*/ NoteBlockInstrument instrument = GlissandoUtils.getInstrument(state).orElse(/*$ instrument >>*/ NoteBlockInstrument .HARP);
		((NoteBlockScreenOpener) player).openScreen(new NoteBlockScreen(clientWorld, pos, instrument));

		return ActionResult.SUCCESS;
	}

	@Override
	public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
		if (!world.isClient() || !GlissandoUtils.isValidNoteBlock(state)) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		if (!(placer instanceof NoteBlockScreenOpener screenOpener) || !GlissandoConfig.get().openScreenWhenPlaced) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		/*$ instrument >>*/ NoteBlockInstrument instrument = GlissandoUtils.getInstrument(state).orElse(/*$ instrument >>*/ NoteBlockInstrument .HARP);
		screenOpener.openScreen(new NoteBlockScreen((ClientWorld) world, pos, instrument));

		super.onPlacedImpl(world, pos, state, placer, itemStack, original);
	}

}