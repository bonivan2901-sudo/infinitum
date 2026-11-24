package com.ivan21.infinitum.entity;

import com.ivan21.infinitum.event.InfinityArmorEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Чёрная дыра: 7 секунд затягивает всё вокруг (мобов убивает, предметы собирает в центр),
 * затем схлопывается и стирает блоки в радиусе 5. Носитель полной брони Бесконечности неуязвим.
 */
public class GapingVoidEntity extends Entity {
    public static final int LIFETIME = 140;
    private static final double PULL_RADIUS = 14.0;
    private static final int COLLAPSE_RADIUS = 5;

    private int age;

    public GapingVoidEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    private boolean isImmune(Entity e) {
        return e instanceof Player p && (p.isCreative() || p.isSpectator() || InfinityArmorEvents.hasFullSet(p));
    }

    @Override
    public void tick() {
        super.tick();
        age++;
        double grow = Math.min(1.0, age / 40.0);

        if (level().isClientSide) {
            spawnClientParticles(grow);
            return;
        }

        if (age == 1) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 2f, 0.3f);
        }
        if (age % 20 == 0) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.PORTAL_AMBIENT, SoundSource.HOSTILE, 1.5f, 0.5f);
        }

        Vec3 center = position();
        double radius = PULL_RADIUS * grow;
        for (Entity e : level().getEntities(this, getBoundingBox().inflate(radius))) {
            if (isImmune(e) || e instanceof GapingVoidEntity) continue;
            Vec3 diff = center.subtract(e.position());
            double dist = diff.length();
            if (dist > radius || dist < 0.01) continue;
            double strength = 0.12 * (1.0 - dist / (radius + 1.0)) + 0.03;
            e.setDeltaMovement(e.getDeltaMovement().scale(0.9).add(diff.normalize().scale(strength)));
            e.hurtMarked = true;
            e.fallDistance = 0;
            if (dist < 1.5 && e instanceof LivingEntity living && !(e instanceof ItemEntity)) {
                living.invulnerableTime = 0;
                living.hurt(damageSources().magic(), 1000f);
            }
        }

        if (age >= LIFETIME) {
            collapse();
            discard();
        }
    }

    private void collapse() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 3f, 0.5f);
        BlockPos c = blockPosition();
        int r = COLLAPSE_RADIUS;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dy * dy + dz * dz > r * r) continue;
                    BlockPos p = c.offset(dx, dy, dz);
                    BlockState s = level().getBlockState(p);
                    if (s.isAir() || s.getDestroySpeed(level(), p) < 0 || level().getBlockEntity(p) != null) continue;
                    level().removeBlock(p, false);
                }
            }
        }
        if (level() instanceof net.minecraft.server.level.ServerLevel sl) {
            sl.sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
            sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), 300, 3, 3, 3, 0.6);
        }
    }

    private void spawnClientParticles(double grow) {
        double r = 0.6 + 4.0 * grow;
        for (int i = 0; i < 8; i++) {
            double a = age * 0.35 + i * (Math.PI / 4);
            double y = (random.nextDouble() - 0.5) * r;
            double px = getX() + Math.cos(a) * r;
            double pz = getZ() + Math.sin(a) * r;
            // частицы закручиваются спиралью к центру
            double vx = (getX() - px) * 0.08 - Math.sin(a) * 0.15;
            double vz = (getZ() - pz) * 0.08 + Math.cos(a) * 0.15;
            level().addParticle(ParticleTypes.PORTAL, px, getY() + y, pz, vx, -y * 0.08, vz);
        }
        level().addParticle(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);
        level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(),
                (random.nextDouble() - .5) * .1, (random.nextDouble() - .5) * .1, (random.nextDouble() - .5) * .1);
        if (age % 3 == 0) {
            level().addParticle(ParticleTypes.END_ROD, getX() + (random.nextDouble() - .5) * r * 2,
                    getY() + (random.nextDouble() - .5) * r, getZ() + (random.nextDouble() - .5) * r * 2, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("VoidAge");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("VoidAge", age);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
