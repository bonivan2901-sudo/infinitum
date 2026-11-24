package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Лук Бесконечности: не нужны стрелы, натягивается в 3 раза быстрее, стрела убивает с одного попадания. */
public class InfinityBowItem extends BowItem {
    public InfinityBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player player)) return;
        int charge = (getUseDuration(stack) - timeLeft) * 3;
        float power = getPowerForTime(charge);
        if (power < 0.1f) return;
        if (!level.isClientSide) {
            for (int i = 0; i < 3; i++) { // три стрелы веером
                Arrow arrow = new Arrow(level, player);
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot() + (i - 1) * 4f, 0f, power * 4.5f, 0f);
                arrow.setBaseDamage(1000.0);
                arrow.setCritArrow(true);
                arrow.setPierceLevel((byte) 10);
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(arrow);
            }
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS, 1f, 1.2f + power * 0.5f);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.bow.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.bow.2").withStyle(ChatFormatting.GRAY));
    }
}
