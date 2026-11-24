package com.ivan21.infinitum.event;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/** Эффекты полного комплекта брони Бесконечности: бессмертие, полёт, сытость, ночное зрение. */
@Mod.EventBusSubscriber(modid = Infinitum.MODID)
public final class InfinityArmorEvents {
    private static final String FLIGHT_TAG = "infinitum_flight";

    private InfinityArmorEvents() {}

    public static boolean hasFullSet(Player p) {
        return p.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.INFINITY_HELMET.get())
                && p.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.INFINITY_CHESTPLATE.get())
                && p.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.INFINITY_LEGGINGS.get())
                && p.getItemBySlot(EquipmentSlot.FEET).is(ModItems.INFINITY_BOOTS.get());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player p = event.player;
        if (event.side == LogicalSide.CLIENT) {
            if (hasFullSet(p) && p.tickCount % 2 == 0) {
                com.ivan21.infinitum.util.InfinityFx.auraSwirl(p.level(), p);
            }
            return;
        }
        CompoundTag data = p.getPersistentData();

        if (hasFullSet(p)) {
            if (!p.getAbilities().mayfly) {
                p.getAbilities().mayfly = true;
                p.onUpdateAbilities();
            }
            data.putBoolean(FLIGHT_TAG, true);

            p.getFoodData().setFoodLevel(20);
            p.getFoodData().setSaturation(20f);
            p.setAirSupply(p.getMaxAirSupply());
            p.clearFire();
            if (p.getHealth() < p.getMaxHealth()) p.setHealth(p.getMaxHealth());

            List<MobEffect> bad = new ArrayList<>();
            for (MobEffectInstance inst : p.getActiveEffects()) {
                if (inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) bad.add(inst.getEffect());
            }
            bad.forEach(p::removeEffect);

            if (p.tickCount % 100 == 0) {
                p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false));
            }
        } else if (data.getBoolean(FLIGHT_TAG)) {
            data.putBoolean(FLIGHT_TAG, false);
            if (!p.isCreative() && !p.isSpectator()) {
                p.getAbilities().mayfly = false;
                p.getAbilities().flying = false;
                p.onUpdateAbilities();
            }
            p.removeEffect(MobEffects.NIGHT_VISION);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player p && hasFullSet(p)) {
            event.setCanceled(true);
        }
    }
}
