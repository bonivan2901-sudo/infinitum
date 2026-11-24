package com.ivan21.infinitum.event;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Infinitum.MODID)
public final class ModEvents {
    private ModEvents() {}

    /** Тотем Бесконечности: отменяет смерть, если лежит где угодно в инвентаре. Не тратится. */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player p) || p.level().isClientSide) return;
        if (!p.getInventory().contains(new ItemStack(ModItems.INFINITY_TOTEM.get()))) return;
        event.setCanceled(true);
        p.setHealth(p.getMaxHealth());
        p.getActiveEffects().stream().filter(e -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                .map(MobEffectInstance::getEffect).toList().forEach(p::removeEffect);
        p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 2));
        p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
        p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0));
        p.clearFire();
        if (p.getY() < p.level().getMinBuildHeight()) { // спасаем из пустоты
            p.teleportTo(p.getX(), p.level().getMaxBuildHeight() - 10, p.getZ());
            p.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0));
        }
        p.level().broadcastEntityEvent(p, (byte) 35); // анимация тотема
        if (p.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.END_ROD, p.getX(), p.getY() + 1, p.getZ(), 80, .6, 1, .6, .2);
        }
        p.displayClientMessage(Component.translatable("message.infinitum.totem_saved")
                .withStyle(ChatFormatting.GOLD), true);
    }

    /** Меч Черепов: скелеты всегда роняют свои черепа. */
    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        LivingEntity dead = event.getEntity();
        if (!(dead instanceof AbstractSkeleton)) return;
        if (!(event.getSource().getEntity() instanceof Player p)) return;
        if (!p.getMainHandItem().is(ModItems.SKULLFIRE_SWORD.get())) return;
        ItemStack skull = new ItemStack(dead instanceof WitherSkeleton ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL);
        event.getDrops().removeIf(e -> e.getItem().is(skull.getItem()));
        event.getDrops().add(new ItemEntity(dead.level(), dead.getX(), dead.getY(), dead.getZ(), skull));
    }
}
