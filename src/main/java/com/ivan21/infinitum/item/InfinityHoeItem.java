package com.ivan21.infinitum.item;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Мотыга Бесконечности: ПКМ — вспахать 9x9, Shift+ПКМ — удобрить всё в радиусе 4. */
public class InfinityHoeItem extends HoeItem {
    private static final int R = 4;

    public InfinityHoeItem(Tier tier, Properties properties) {
        super(tier, 20, 0f, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos center = ctx.getClickedPos();
        if (!(level instanceof ServerLevel sl)) return InteractionResult.SUCCESS;

        int done = 0;
        boolean fertilize = player != null && player.isShiftKeyDown();
        for (int dx = -R; dx <= R; dx++) {
            for (int dz = -R; dz <= R; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    BlockState s = level.getBlockState(p);
                    if (fertilize) {
                        if (BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), level, p, player)) {
                            sl.sendParticles(ParticleTypes.HAPPY_VILLAGER, p.getX() + .5, p.getY() + .8, p.getZ() + .5, 4, .3, .2, .3, 0);
                            done++;
                        }
                    } else if ((s.is(Blocks.GRASS_BLOCK) || s.is(Blocks.DIRT) || s.is(Blocks.DIRT_PATH)
                            || s.is(Blocks.COARSE_DIRT) || s.is(Blocks.ROOTED_DIRT))
                            && level.getBlockState(p.above()).isAir()) {
                        level.setBlock(p, Blocks.FARMLAND.defaultBlockState(), 11);
                        done++;
                    }
                }
            }
        }
        if (done > 0) {
            level.playSound(null, center, fertilize ? SoundEvents.BONE_MEAL_USE : SoundEvents.HOE_TILL,
                    SoundSource.BLOCKS, 1f, 1f);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        InfinityFx.handSparkles(level, entity, selected, ParticleTypes.HAPPY_VILLAGER, 6);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.hoe.1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.infinitum.hoe.2").withStyle(ChatFormatting.GRAY));
    }
}
