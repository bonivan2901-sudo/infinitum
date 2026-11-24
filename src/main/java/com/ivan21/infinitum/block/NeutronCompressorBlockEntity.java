package com.ivan21.infinitum.block;

import com.ivan21.infinitum.registry.ModBlockEntities;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class NeutronCompressorBlockEntity extends BlockEntity {
    /** Сколько блоков нужно на одну сингулярность. */
    public static final int COST = 256;
    private static Map<Item, Item> recipes;

    private Item input = Items.AIR;
    private int count;

    public NeutronCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEUTRON_COMPRESSOR.get(), pos, state);
    }

    private static Map<Item, Item> recipes() {
        if (recipes == null) {
            Map<Item, Item> m = new LinkedHashMap<>();
            m.put(Items.IRON_BLOCK, ModItems.IRON_SINGULARITY.get());
            m.put(Items.GOLD_BLOCK, ModItems.GOLD_SINGULARITY.get());
            m.put(Items.COPPER_BLOCK, ModItems.COPPER_SINGULARITY.get());
            m.put(Items.REDSTONE_BLOCK, ModItems.REDSTONE_SINGULARITY.get());
            m.put(Items.LAPIS_BLOCK, ModItems.LAPIS_SINGULARITY.get());
            m.put(Items.DIAMOND_BLOCK, ModItems.DIAMOND_SINGULARITY.get());
            m.put(Items.EMERALD_BLOCK, ModItems.EMERALD_SINGULARITY.get());
            m.put(Items.NETHERITE_BLOCK, ModItems.NETHERITE_SINGULARITY.get());
            recipes = m;
        }
        return recipes;
    }

    public void interact(Player player, ItemStack held) {
        if (held.isEmpty()) {
            if (count == 0) {
                player.displayClientMessage(Component.translatable("message.infinitum.compressor_empty")
                        .withStyle(ChatFormatting.GRAY), true);
            } else {
                player.displayClientMessage(Component.translatable("message.infinitum.compressor_progress",
                        input.getDescription(), count, COST).withStyle(ChatFormatting.AQUA), true);
            }
            return;
        }
        Item out = recipes().get(held.getItem());
        if (out == null) {
            player.displayClientMessage(Component.translatable("message.infinitum.compressor_wrong")
                    .withStyle(ChatFormatting.RED), true);
            return;
        }
        if (count > 0 && input != held.getItem()) {
            player.displayClientMessage(Component.translatable("message.infinitum.compressor_busy",
                    input.getDescription()).withStyle(ChatFormatting.RED), true);
            return;
        }
        input = held.getItem();
        int take = Math.min(held.getCount(), COST - count);
        held.shrink(take);
        count += take;
        setChanged();
        if (level == null) return;
        level.playSound(null, worldPosition, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.6f, 0.6f);

        if (count >= COST) {
            count = 0;
            input = Items.AIR;
            ItemEntity drop = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.1,
                    worldPosition.getZ() + 0.5, new ItemStack(out));
            drop.setDeltaMovement(0, 0.25, 0);
            level.addFreshEntity(drop);
            level.playSound(null, worldPosition, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 1.5f);
            if (level instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.END_ROD, worldPosition.getX() + .5, worldPosition.getY() + 1.2,
                        worldPosition.getZ() + .5, 60, .4, .4, .4, .15);
            }
            player.displayClientMessage(Component.translatable("message.infinitum.compressor_done", out.getDescription())
                    .withStyle(ChatFormatting.LIGHT_PURPLE), true);
        } else {
            player.displayClientMessage(Component.translatable("message.infinitum.compressor_progress",
                    input.getDescription(), count, COST).withStyle(ChatFormatting.AQUA), true);
        }
    }

    public void dropContents(Level level, BlockPos pos) {
        while (count > 0 && input != Items.AIR) {
            int n = Math.min(64, count);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(input, n));
            count -= n;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(input);
        tag.putString("input", id == null ? "minecraft:air" : id.toString());
        tag.putInt("count", count);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("input")));
        input = it == null ? Items.AIR : it;
        count = tag.getInt("count");
    }
}
