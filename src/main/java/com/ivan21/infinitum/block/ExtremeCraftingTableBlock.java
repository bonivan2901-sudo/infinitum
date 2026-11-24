package com.ivan21.infinitum.block;

import com.ivan21.infinitum.menu.ExtremeCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Экстремальный верстак — сетка 9x9 для крафта предметов Бесконечности. */
public class ExtremeCraftingTableBlock extends Block {
    private static final Component TITLE = Component.translatable("container.infinitum.extreme_crafting");

    public ExtremeCraftingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inv, player) ->
                new ExtremeCraftingMenu(id, inv, ContainerLevelAccess.create(level, pos)), TITLE);
    }
}
