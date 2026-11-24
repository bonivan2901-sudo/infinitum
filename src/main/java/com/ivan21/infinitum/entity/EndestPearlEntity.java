package com.ivan21.infinitum.entity;

import com.ivan21.infinitum.event.InfinityArmorEvents;
import com.ivan21.infinitum.registry.ModEntities;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Эндовейшая жемчужина: при попадании открывает «чёрную дыру», которая всё засасывает и стирает. */
public class EndestPearlEntity extends ThrowableItemProjectile {
    private static final double PULL_RADIUS = 16.0;
    private static final double KILL_RADIUS = 6.0;
    private static final int EAT_RADIUS = 4;

    public EndestPearlEntity(EntityType<? extends EndestPearlEntity> type, Level level) {
        super(type, level);
    }

    public EndestPearlEntity(Level level, LivingEntity owner) {
        super(ModEntities.ENDEST_PEARL.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ENDEST_PEARL.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                level().addParticle(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(),
                        (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level() instanceof ServerLevel sl) {
            blackHole(sl, position());
            discard();
        }
    }

    private void blackHole(ServerLevel sl, Vec3 c) {
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, c.x, c.y, c.z, 400, 3, 3, 3, 0.6);
        sl.sendParticles(ParticleTypes.SQUID_INK, c.x, c.y, c.z, 120, 1.5, 1.5, 1.5, 0.05);
        sl.sendParticles(ParticleTypes.SONIC_BOOM, c.x, c.y, c.z, 1, 0, 0, 0, 0);
        sl.playSound(null, c.x, c.y, c.z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2f, 0.5f);
        sl.playSound(null, c.x, c.y, c.z, SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 1f, 1.6f);

        Entity owner = getOwner();
        AABB box = new AABB(c, c).inflate(PULL_RADIUS);
        for (Entity e : sl.getEntities(this, box)) {
            if (e == owner) continue;
            if (e instanceof Player p && (p.isCreative() || p.isSpectator() || InfinityArmorEvents.hasFullSet(p))) continue;
            Vec3 toCenter = c.subtract(e.position());
            double dist = toCenter.length();
            if (dist < KILL_RADIUS) {
                if (e instanceof LivingEntity le) {
                    le.invulnerableTime = 0;
                    le.hurt(damageSources().thrown(this, owner), 1_000_000f);
                    if (le.isAlive()) le.setHealth(0f);
                } else if (e instanceof ItemEntity) {
                    e.discard();
                }
            } else {
                e.setDeltaMovement(e.getDeltaMovement().add(toCenter.normalize().scale(1.6)));
                e.hurtMarked = true;
            }
        }

        if (!sl.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return;
        BlockPos center = BlockPos.containing(c);
        for (int dx = -EAT_RADIUS; dx <= EAT_RADIUS; dx++)
            for (int dy = -EAT_RADIUS; dy <= EAT_RADIUS; dy++)
                for (int dz = -EAT_RADIUS; dz <= EAT_RADIUS; dz++) {
                    if (dx * dx + dy * dy + dz * dz > EAT_RADIUS * EAT_RADIUS) continue;
                    BlockPos p = center.offset(dx, dy, dz);
                    BlockState s = sl.getBlockState(p);
                    if (s.isAir() || s.getDestroySpeed(sl, p) < 0 || sl.getBlockEntity(p) != null) continue;
                    sl.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
                }
    }
}
