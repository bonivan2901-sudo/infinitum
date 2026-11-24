package com.ivan21.infinitum.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/** Визуальные эффекты: искры вокруг предметов, брони и блоков. Только клиент. */
public final class InfinityFx {
    private InfinityFx() {}

    /** Искры у руки, когда предмет выбран. Вызывать из Item#inventoryTick. */
    public static void handSparkles(Level level, Entity holder, boolean selected, ParticleOptions particle, int chance) {
        if (!level.isClientSide || !selected) return;
        RandomSource r = level.random;
        if (r.nextInt(chance) != 0) return;
        double yaw = Math.toRadians(holder.getYRot());
        double hx = holder.getX() - Math.sin(yaw) * 0.5 - Math.cos(yaw) * 0.4;
        double hz = holder.getZ() + Math.cos(yaw) * 0.5 - Math.sin(yaw) * 0.4;
        double hy = holder.getY() + holder.getBbHeight() * 0.55;
        level.addParticle(particle, hx + (r.nextDouble() - 0.5) * 0.4, hy + r.nextDouble() * 0.6,
                hz + (r.nextDouble() - 0.5) * 0.4, 0, 0.02, 0);
    }

    public static void handSparkles(Level level, Entity holder, boolean selected) {
        handSparkles(level, holder, selected, ParticleTypes.END_ROD, 3);
    }

    /** Вихрь частиц вокруг всего тела (полный комплект брони). */
    public static void auraSwirl(Level level, Entity e) {
        RandomSource r = level.random;
        double t = (e.tickCount % 40) / 40.0 * Math.PI * 2;
        for (int i = 0; i < 2; i++) {
            double a = t + i * Math.PI;
            level.addParticle(ParticleTypes.END_ROD,
                    e.getX() + Math.cos(a) * 0.8, e.getY() + (e.tickCount % 20) / 10.0, e.getZ() + Math.sin(a) * 0.8,
                    0, 0.01, 0);
        }
        if (r.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.ENCHANT, e.getX() + (r.nextDouble() - 0.5),
                    e.getY() + r.nextDouble() * 2, e.getZ() + (r.nextDouble() - 0.5), 0, 0.3, 0);
        }
    }

    /** Мерцание над блоком. Вызывать из Block#animateTick. */
    public static void blockSparkles(Level level, BlockPos pos, RandomSource r, ParticleOptions particle, int count) {
        for (int i = 0; i < count; i++) {
            level.addParticle(particle, pos.getX() + r.nextDouble(), pos.getY() + 1.0 + r.nextDouble() * 0.3,
                    pos.getZ() + r.nextDouble(), (r.nextDouble() - 0.5) * 0.02, 0.03, (r.nextDouble() - 0.5) * 0.02);
        }
    }
}
