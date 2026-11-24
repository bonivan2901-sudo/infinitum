package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Часы Бесконечности: ПКМ — следующее время суток, Shift+ПКМ — остановить/запустить время. */
public class InfinityClockItem extends Item {
    private static final long[] PHASES = {1000, 6000, 12000, 18000};
    private static final String[] NAMES = {"morning", "noon", "evening", "midnight"};

    public InfinityClockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel sl) {
            if (player.isShiftKeyDown()) {
                GameRules.BooleanValue rule = sl.getGameRules().getRule(GameRules.RULE_DAYLIGHT);
                boolean frozen = rule.get();
                rule.set(!frozen, sl.getServer());
                player.displayClientMessage(Component.translatable(frozen
                        ? "message.infinitum.time_frozen" : "message.infinitum.time_running")
                        .withStyle(ChatFormatting.AQUA), true);
            } else {
                long time = sl.getDayTime();
                long day = time / 24000L;
                long inDay = time % 24000L;
                int next = 0;
                for (int i = 0; i < PHASES.length; i++) {
                    if (PHASES[i] > inDay + 100) { next = i; break; }
                    if (i == PHASES.length - 1) { next = 0; day++; }
                }
                sl.setDayTime(day * 24000L + PHASES[next]);
                player.displayClientMessage(Component.translatable("message.infinitum.time_" + NAMES[next])
                        .withStyle(ChatFormatting.AQUA), true);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1f, 1.2f);
        }
        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.clock.1").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("tooltip.infinitum.clock.2").withStyle(ChatFormatting.GRAY));
    }
}
