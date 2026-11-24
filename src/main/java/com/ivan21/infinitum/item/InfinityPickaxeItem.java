package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.AreaBreaker;
import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Разрушитель миров: мгновенно ломает любой блок (кроме бедрока) и всегда даёт дроп.
 * Shift + ПКМ в воздух — режим 5x5x5.
 */
public class InfinityPickaxeItem extends PickaxeItem {
    private static final int RADIUS = 2;

    public InfinityPickaxeItem(Tier tier, Properties properties) {
        super(tier, 40, -2.8f, properties);
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) return InteractionResultHolder.pass(stack);
        if (!level.isClientSide) {
            boolean now = AreaBreaker.toggle(stack);
            player.displayClientMessage(Component.translatable(now
                    ? "message.infinitum.hammer_on" : "message.infinitum.hammer_off")
                    .withStyle(ChatFormatting.LIGHT_PURPLE), true);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.7f, now ? 1.5f : 0.8f);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /** Forge-хук: вызывается перед тем, как игрок сломает блок. */
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        if (AreaBreaker.isAreaMode(stack) && !player.isShiftKeyDown() && player.level() instanceof ServerLevel level) {
            AreaBreaker.breakCube(level, pos, player, RADIUS, s -> true);
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false; // своя анимированная текстура вместо стандартного блеска
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.pickaxe.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.pickaxe.2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(AreaBreaker.isAreaMode(stack)
                ? "tooltip.infinitum.pickaxe.mode_on" : "tooltip.infinitum.pickaxe.mode_off")
                .withStyle(ChatFormatting.GOLD));
    }
}
