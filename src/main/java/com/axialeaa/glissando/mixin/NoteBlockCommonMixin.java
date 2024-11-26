package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world. /*$ get_state_with_instrument_world_param >>*/ WorldAccess ;
import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

@Mixin(NoteBlock.class)
public class NoteBlockCommonMixin {

	@Shadow @Final public static EnumProperty</*$ instrument >>*/ NoteBlockInstrument > INSTRUMENT;

	@ModifyExpressionValue(method = "onUse", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"))
	private boolean shouldCycleStateOnUse(boolean original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
		SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);
		return original || SerializableNoteBlockInstrument.canOpenNoteBlockScreen(world, pos, instrument);
	}

	@ModifyExpressionValue(method = /*$ on_use_with_item >>*/ "onUseWithItem" , at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean shouldPassBlockAction(boolean original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
		return original && world.getBlockState(pos.up()).isReplaceable();
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 0))
	private Object removeInstrumentProperty(BlockState instance, Property<?> property, Comparable<?> comparable, Operation<BlockState> original) {
		return instance;
	}

	@WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NoteBlock;getStateWithInstrument(Lnet/minecraft/world/" + /*$ get_state_with_instrument_world_param_string >>*/ "WorldAccess" + ";Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"))
	private BlockState getDefaultStateForPlacement(NoteBlock instance, /*$ get_state_with_instrument_world_param >>*/ WorldAccess world, BlockPos pos, BlockState state, Operation<BlockState> original) {
		return state;
	}

	@ModifyExpressionValue(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;getAxis()Lnet/minecraft/util/math/Direction$Axis;"))
	private Direction.Axis bypassInstrumentChange(Direction.Axis original) {
		return Direction.Axis.X;
	}

	/**
	 * @author axialeaa
	 * @reason The note block instrument object has completely changed. There is nothing "work-around-able" about the vanilla method.
	 */
	@Overwrite
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);
		return instrument.playSoundAndAddParticle(world, pos, state);
	}

	@WrapOperation(method = "playNote", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"))
	private Comparable<?> imitateBehaviorForNewObject(BlockState instance, Property<?> property, Operation<Comparable<?>> original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
		SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);
        return instrument.isTop(world) ? /*$ instrument >>*/ NoteBlockInstrument .ZOMBIE : /*$ instrument >>*/ NoteBlockInstrument .HARP;
	}

	@WrapOperation(method = "appendProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/state/StateManager$Builder;add([Lnet/minecraft/state/property/Property;)Lnet/minecraft/state/StateManager$Builder;"))
	private StateManager.Builder<Block, BlockState> removeInstrumentProperty(StateManager.Builder<Block, BlockState> instance, Property<?>[] properties, Operation<StateManager.Builder<Block, BlockState>> original) {
		List<Property<?>> list = new ArrayList<>(List.of(properties));
		list.remove(INSTRUMENT);

		return original.call(instance, list.toArray(Property<?>[]::new));
	}

}