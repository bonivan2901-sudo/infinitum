package com.ivan21.infinitum.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/** Массовое разрушение блоков для инструментов Бесконечности. */
public final class AreaBreaker {
    public static final String MODE_TAG = "infinitum_area";

    private AreaBreaker() {}

    public static boolean isAreaMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(MODE_TAG);
    }

    public static boolean toggle(ItemStack stack) {
        boolean now = !isAreaMode(stack);
        stack.getOrCreateTag().putBoolean(MODE_TAG, now);
        return now;
    }

    private static boolean breakable(ServerLevel level, BlockPos p, BlockState s) {
        return !s.isAir() && s.getDestroySpeed(level, p) >= 0 && level.getBlockEntity(p) == null;
    }

    /** Ломает куб (2r+1)^3 вокруг pos (сам pos ломает игра). */
    public static void breakCube(ServerLevel level, BlockPos pos, Player player, int r, Predicate<BlockState> filter) {
        for (int dx = -r; dx <= r; dx++)
            for (int dy = -r; dy <= r; dy++)
                for (int dz = -r; dz <= r; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos p = pos.offset(dx, dy, dz);
                    BlockState s = level.getBlockState(p);
                    if (breakable(level, p, s) && filter.test(s)) level.destroyBlock(p, true, player);
                }
    }

    /** Ломает все связанные блоки, подходящие под фильтр (рубка дерева целиком). */
    public static void breakConnected(ServerLevel level, BlockPos start, Player player, int limit, Predicate<BlockState> filter) {
        Set<BlockPos> seen = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);
        int broken = 0;
        while (!queue.isEmpty() && broken < limit) {
            BlockPos cur = queue.poll();
            for (int dx = -1; dx <= 1; dx++)
                for (int dy = -1; dy <= 1; dy++)
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos n = cur.offset(dx, dy, dz);
                        if (!seen.add(n)) continue;
                        BlockState s = level.getBlockState(n);
                        if (breakable(level, n, s) && filter.test(s)) {
                            queue.add(n);
                            level.destroyBlock(n, true, player);
                            broken++;
                        }
                    }
        }
    }
}
