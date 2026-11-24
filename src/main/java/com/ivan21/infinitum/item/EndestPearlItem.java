package com.ivan21.infinitum.item;

import com.ivan21.infinitum.entity.EndestPearlEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndestPearlItem extends Item {
    public EndestPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW,
                SoundSource.NEUTRAL, 0.5f, 0.4f);
        if (!level.isClientSide) {
            EndestPearlEntity pearl = new EndestPearlEntity(level, player);
            pearl.setItem(stack);
            pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 1.5f, 1f);
            level.addFreshEntity(pearl);
        }
        player.getCooldowns().addCooldown(this, 40);
        if (!player.getAbilities().instabuild) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.endest.1").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.endest.2").withStyle(ChatFormatting.GRAY));
    }
}
