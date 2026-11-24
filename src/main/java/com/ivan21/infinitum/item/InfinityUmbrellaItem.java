package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Зонтик Бесконечности: ПКМ — ясно → дождь → гроза, Shift+ПКМ — заморозить погоду.
 * Если держать в руке в падении — плавно планируешь, как с настоящим зонтом.
 */
public class InfinityUmbrellaItem extends Item {
    public InfinityUmbrellaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel sl) {
            String key;
            if (player.isShiftKeyDown()) {
                GameRules.BooleanValue rule = sl.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE);
                boolean was = rule.get();
                rule.set(!was, sl.getServer());
                key = was ? "message.infinitum.weather_frozen" : "message.infinitum.weather_running";
            } else if (sl.isThundering()) {
                sl.setWeatherParameters(24000 * 5, 0, false, false);
                key = "message.infinitum.weather_clear";
            } else if (sl.isRaining()) {
                sl.setWeatherParameters(0, 24000 * 5, true, true);
                key = "message.infinitum.weather_thunder";
            } else {
                sl.setWeatherParameters(0, 24000 * 5, true, false);
                key = "message.infinitum.weather_rain";
            }
            player.displayClientMessage(Component.translatable(key).withStyle(ChatFormatting.AQUA), true);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS, 0.4f, 1.8f);
        }
        player.getCooldowns().addCooldown(this, 10);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        boolean inHand = selected || (entity instanceof LivingEntity holder && holder.getOffhandItem() == stack);
        if (!inHand || !(entity instanceof LivingEntity le)) return;
        if (!le.onGround() && le.getDeltaMovement().y < -0.1) {
            if (!level.isClientSide) {
                le.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 10, 0, false, false));
            } else if (level.random.nextInt(2) == 0) {
                level.addParticle(ParticleTypes.CLOUD, le.getX(), le.getY() + 2.3, le.getZ(), 0, -0.05, 0);
            }
        }
        InfinityFx.handSparkles(level, entity, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.umbrella.1").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("tooltip.infinitum.umbrella.2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.infinitum.umbrella.3").withStyle(ChatFormatting.GRAY));
    }
}
