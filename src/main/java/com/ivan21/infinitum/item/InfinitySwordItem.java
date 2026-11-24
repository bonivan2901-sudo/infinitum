package com.ivan21.infinitum.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Меч Бесконечности: убивает любого моба одним ударом.
 * ПКМ — «Волна пустоты»: уничтожает всех враждебных мобов в радиусе 24 блоков.
 */
public class InfinitySwordItem extends SwordItem {
    private static final float KILL_DAMAGE = 1_000_000f;

    public InfinitySwordItem(Tier tier, Properties properties) {
        super(tier, 9999, -2.0f, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide) {
            obliterate(target, attacker);
        }
        return true;
    }

    private static void obliterate(LivingEntity target, LivingEntity attacker) {
        if (target instanceof Player p && (p.isCreative() || p.isSpectator())) {
            return;
        }
        DamageSource source = attacker instanceof Player player
                ? attacker.damageSources().playerAttack(player)
                : attacker.damageSources().mobAttack(attacker);
        target.invulnerableTime = 0;
        target.hurt(source, KILL_DAMAGE);
        if (target.isAlive()) {
            target.setHealth(0f);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(24.0), e -> e instanceof Enemy && e.isAlive());
            for (LivingEntity e : enemies) {
                obliterate(e, player);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.8f, 1.6f);
            player.displayClientMessage(Component.translatable("message.infinitum.sword_wave", enemies.size())
                    .withStyle(ChatFormatting.LIGHT_PURPLE), true);
        }
        player.getCooldowns().addCooldown(this, 20);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        com.ivan21.infinitum.util.InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.sword.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.sword.2").withStyle(ChatFormatting.GRAY));
    }
}
