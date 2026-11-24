package com.ivan21.infinitum.item;

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

/**
 * Кристалл дюпа.
 * 1) ПКМ с кристаллом в основной руке — копирует весь стак из второй руки (вместе с зачарованиями и NBT).
 * 2) В верстаке: кристалл + любой предмет -> копия стака, а кристалл и оригинал остаются.
 * Кристалл не тратится.
 */
public class DuplicationCrystalItem extends Item {
    public DuplicationCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack crystal = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(crystal);
        }
        ItemStack target = player.getOffhandItem();
        if (target.isEmpty() || target.getItem() instanceof DuplicationCrystalItem) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.infinitum.dupe_empty")
                        .withStyle(ChatFormatting.RED), true);
            }
            return InteractionResultHolder.fail(crystal);
        }
        if (!level.isClientSide) {
            ItemStack copy = target.copy();
            if (!player.getInventory().add(copy)) {
                player.drop(copy, false);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0f, 1.4f);
            player.displayClientMessage(Component.translatable("message.infinitum.duped",
                    target.getCount(), target.getHoverName()).withStyle(ChatFormatting.LIGHT_PURPLE), true);
        }
        return InteractionResultHolder.sidedSuccess(crystal, level.isClientSide);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.dupe.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.dupe.2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.infinitum.dupe.3").withStyle(ChatFormatting.GRAY));
    }
}
