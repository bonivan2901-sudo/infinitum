package com.ivan21.infinitum.block;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Нейтронный компрессор: ПКМ стаком блоков (железо, золото, алмазы...) — закидывает их внутрь.
 * Набрав нужное количество, выдаёт сингулярность. ПКМ пустой рукой — показать прогресс.
 */
public class NeutronCompressorBlock extends BaseEntityBlock {
    public NeutronCompressorBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NeutronCompressorBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof NeutronCompressorBlockEntity be) {
            be.interact(player, player.getItemInHand(hand));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) InfinityFx.blockSparkles(level, pos, random, ParticleTypes.PORTAL, 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof NeutronCompressorBlockEntity be) {
            be.dropContents(level, pos);
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}
