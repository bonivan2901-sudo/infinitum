package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Меч Черепов: поджигает, а скелеты-иссушители всегда роняют череп (см. ModEvents). */
public class SkullfireSwordItem extends SwordItem {
    public SkullfireSwordItem(Tier tier, Properties properties) {
        super(tier, 22, -2.4f, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setSecondsOnFire(10);
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected, ParticleTypes.FLAME, 2);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.skullfire.1").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.infinitum.skullfire.2").withStyle(ChatFormatting.GRAY));
    }
}
