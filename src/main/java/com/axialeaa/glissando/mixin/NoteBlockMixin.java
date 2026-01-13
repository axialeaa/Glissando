package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.mixin.impl.BlockImplMixin;
import com.axialeaa.glissando.payload.TuneNoteBlockC2SPayload;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.WorldView ;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(NoteBlock.class)
public class NoteBlockMixin extends BlockImplMixin implements BlockEntityProvider {

	@Shadow @Final public static EnumProperty<net.minecraft.block.enums.NoteBlockInstrument> INSTRUMENT;
	@Shadow @Final public static IntProperty NOTE;

	@ModifyExpressionValue(method = "onUseWithItem" , at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean shouldPassBlockAction(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
		Block block = Block.getBlockFromItem(stack.getItem());
		BlockState blockState = block.getDefaultState();

		return original && blockState.canPlaceAt(world, pos.up());
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 0))
	private Object removeInstrumentProperty(BlockState instance, Property<?> property, Comparable<?> comparable, Operation<BlockState> original) {
		return instance;
	}

	@WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NoteBlock;getStateWithInstrument(Lnet/minecraft/world/" + /*$ get_state_with_instrument_world_param_string >>*/ "WorldView" + ";Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"))
	private BlockState getDefaultStateForPlacement(NoteBlock instance, WorldView world, BlockPos pos, BlockState state, Operation<BlockState> original) {
		return state;
	}

	@Inject(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NoteBlock;getStateWithInstrument(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"), cancellable = true)
	private void bypassInstrumentChange(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random, CallbackInfoReturnable<BlockState> cir, @Local boolean powered) {
		if (world.getBlockEntity(pos) instanceof NoteBlockEntity blockEntity && !world.isClient() && powered) {
			Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);
			RegistryKey<DataNoteBlockInstrument> registryKey = blockEntity.instrumentFormationValidationHandler.findSimpleInstrument((ServerWorld) world, pos, state, registry);

			blockEntity.updateInstrumentKey((ServerWorld) world, pos, registryKey, true);
		}

		cir.setReturnValue(state);
	}

	@WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NoteBlock;playNote(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
	private void playNote(NoteBlock instance, Entity entity, BlockState state, World world, BlockPos pos, Operation<Void> original) {
		NoteBlockEntity blockEntity = (NoteBlockEntity) world.getBlockEntity(pos);

		if (blockEntity == null)
			return;

		if (!world.isClient())
			TuneNoteBlockC2SPayload.onPlay((ServerWorld) world, pos, state, blockEntity, state.get(NOTE).shortValue());

		world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos);
	}

	/**
	 * @author axialeaa
	 * @reason Too much changed :c
	 */
	@Overwrite
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		NoteBlockEntity blockEntity = (NoteBlockEntity) world.getBlockEntity(pos);

		if (blockEntity == null || world.isClient() || blockEntity.isOtherPlayerEditing(player))
			return ActionResult.SUCCESS;

		if (!world.isClient() && blockEntity.canOpenNoteBlockScreen((ServerWorld) world, pos, state, player.getUuid())) {
			blockEntity.findAndUpdateInstrument((ServerWorld) world, pos, state, false);
			blockEntity.openScreen((ServerPlayerEntity) player, pos, false);

			return ActionResult.SUCCESS;
		}

		state = state.cycle(NOTE);
		world.setBlockState(pos, state, Block.NOTIFY_ALL);

		if (!world.isClient())
			TuneNoteBlockC2SPayload.onPlay((ServerWorld) world, pos, state, blockEntity, state.get(NOTE).shortValue());

		player.incrementStat(Stats.TUNE_NOTEBLOCK);
		world.emitGameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);

		return ActionResult.SUCCESS;
	}

	@WrapOperation(method = "appendProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager$Builder;add([Lnet/minecraft/state/property/Property;)Lnet/minecraft/state/StateManager$Builder;"))
	private StateManager.Builder<Block, BlockState> removeInstrumentProperty(StateManager.Builder<Block, BlockState> instance, Property<?>[] properties, Operation<StateManager.Builder<Block, BlockState>> original) {
		List<Property<?>> list = new ArrayList<>(List.of(properties));
		list.remove(INSTRUMENT);

		return original.call(instance, list.toArray(Property<?>[]::new));
	}

	@Override
	public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
		super.onPlacedImpl(world, pos, state, placer, itemStack, original);

		if (world.isClient() || !(world.getBlockEntity(pos) instanceof NoteBlockEntity blockEntity) || !(placer instanceof PlayerEntity player))
			return;

		blockEntity.findAndUpdateInstrument((ServerWorld) world, pos, state, false);

		if (blockEntity.canOpenNoteBlockScreen((ServerWorld) world, pos, state, player.getUuid()))
			blockEntity.openScreen((ServerPlayerEntity) player, pos, true);
	}

	@Override
	public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
		super.onBlockAddedImpl(state, world, pos, oldState, notify, original);
	}

	@Override
	public BlockRenderType getRenderTypeImpl(BlockState state, Operation<BlockRenderType> original) {
		return BlockRenderType.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new NoteBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (type != NoteBlockEntity.TYPE || world.isClient())
			return null;

		Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);

		if (registry.stream().filter(DataNoteBlockInstrument::isMultiBlock).findFirst().isEmpty())
			return null;

		return (world1, pos, state1, blockEntity) -> NoteBlockEntity.tick((ServerWorld) world1, pos, state1, (NoteBlockEntity) blockEntity);
	}

}