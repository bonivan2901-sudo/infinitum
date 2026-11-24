package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.AreaBreaker;
import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Топор Бесконечности: срубает всё дерево целиком вместе с листвой (Shift — один блок). */
public class InfinityAxeItem extends AxeItem {
    public InfinityAxeItem(Tier tier, Properties properties) {
        super(tier, 60f, -3.0f, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 9999f;
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

    public boolean isCorrectToolForDrops(BlockState state) {
        return true;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        if (player.isShiftKeyDown() || !(player.level() instanceof ServerLevel level)) return false;
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.LOGS)) {
            AreaBreaker.breakConnected(level, pos, player, 1024,
                    s -> s.is(BlockTags.LOGS) || s.is(BlockTags.LEAVES) || s.is(BlockTags.WART_BLOCKS));
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.axe.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.axe.2").withStyle(ChatFormatting.GRAY));
    }
}
