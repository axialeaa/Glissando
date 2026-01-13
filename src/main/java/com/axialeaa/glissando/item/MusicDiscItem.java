package com.axialeaa.glissando.item;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.registry.GlissandoSoundEvents;
import com.axialeaa.glissando.data.registry.RegistryUtils;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.axialeaa.glissando.item.component.RecordingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class MusicDiscItem extends Item {

    private static final int USE_COOLDOWN = 40;
    public static final String PATH = "music_disc";

    public static final MusicDiscItem REGISTRY_INSTANCE = new MusicDiscItem(new Item.Settings()
        .maxCount(1)
        .rarity(Rarity.UNCOMMON)
        .component(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT)
        .component(RecordingComponent.TYPE, RecordingComponent.DEFAULT)
        .registryKey(RegistryUtils.keyOf(RegistryKeys.ITEM, PATH))
    );

    public MusicDiscItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user.isSneaking())
            this.tryOpenScreen(world, hand, itemStack);
        else if (world.isClient())
            world.playSoundFromEntityClient(user, GlissandoSoundEvents.MUSIC_DISC_SCRATCH, SoundCategory.RECORDS, 1, 1);
        else {
            this.toggleRecording((ServerWorld) world, user, itemStack, (DiscHolder) user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return ActionResult.SUCCESS;
    }

    private static boolean isCustomComponentDataEmpty(ItemStack stack) {
        return stack.getOrDefault(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT).data().isEmpty();
    }

    private void tryOpenScreen(World world, Hand hand, ItemStack stack) {
        if (!world.isClient())
            return;

        MinecraftClient.getInstance().setScreen(new CustomMusicDiscScreen(hand, stack));
        Glissando.printDebugLog(logger -> logger.info("Attempted to open custom music disc screen"));
    }

    private void toggleRecording(ServerWorld world, PlayerEntity user, ItemStack stack, DiscHolder holder) {
        if (stack.getOrDefault(RecordingComponent.TYPE, RecordingComponent.DEFAULT).recording())
            this.stopRecording(user, stack, holder);
        else if (isCustomComponentDataEmpty(stack))
            this.startRecording(world, user, stack, holder);
    }

    private void stopRecording(PlayerEntity user, ItemStack stack, DiscHolder holder) {
        stack.set(RecordingComponent.TYPE, RecordingComponent.DEFAULT);
        holder.getRecordingStacks().remove(stack);

        user.sendMessage(Text.translatable("item.glissando.music_disc.stop_recording"), true);
    }

    private void startRecording(ServerWorld world, PlayerEntity user, ItemStack stack, DiscHolder holder) {
        stack.set(RecordingComponent.TYPE, new RecordingComponent(true, world.getTime()));
        holder.getRecordingStacks().add(stack);

        user.getItemCooldownManager().set(stack, USE_COOLDOWN);
        user.sendMessage(Text.translatable("item.glissando.music_disc.start_recording"), true);
    }

}
