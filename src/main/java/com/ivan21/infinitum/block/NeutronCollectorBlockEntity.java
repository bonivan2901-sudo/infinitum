package com.ivan21.infinitum.block;

import com.ivan21.infinitum.registry.ModBlockEntities;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronCollectorBlockEntity extends BlockEntity {
    /** 600 тиков = 30 секунд на одну нейтронную пыль. */
    public static final int TICKS_PER_PILE = 600;
    public static final int MAX_STORED = 64 * 9;

    private int progress;
    private int stored;

    public NeutronCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEUTRON_COLLECTOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NeutronCollectorBlockEntity be) {
        if (be.stored >= MAX_STORED) return;
        if (++be.progress >= TICKS_PER_PILE) {
            be.progress = 0;
            be.stored++;
            be.setChanged();
        }
    }

    public void giveTo(Player player) {
        if (stored <= 0) {
            int seconds = (TICKS_PER_PILE - progress) / 20;
            player.displayClientMessage(Component.translatable("message.infinitum.collector_empty", seconds)
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        int given = stored;
        while (stored > 0) {
            int n = Math.min(64, stored);
            ItemStack stack = new ItemStack(ModItems.NEUTRON_PILE.get(), n);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
            stored -= n;
        }
        setChanged();
        player.displayClientMessage(Component.translatable("message.infinitum.collector_take", given)
                .withStyle(ChatFormatting.DARK_AQUA), true);
        if (level != null) {
            level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 0.8f, 0.6f);
        }
    }

    public void dropContents(Level level, BlockPos pos) {
        while (stored > 0) {
            int n = Math.min(64, stored);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.NEUTRON_PILE.get(), n));
            stored -= n;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress", progress);
        tag.putInt("stored", stored);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
        stored = tag.getInt("stored");
    }
}
