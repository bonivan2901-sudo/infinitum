package com.ivan21.infinitum.block;

import com.ivan21.infinitum.util.InfinityFx;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/** Блок, над которым мерцают частицы (блоки Бесконечности и кристальной матрицы). */
public class SparkleBlock extends Block {
    private final Supplier<ParticleOptions> particle;
    private final int chance;

    public SparkleBlock(Properties properties, Supplier<ParticleOptions> particle, int chance) {
        super(properties);
        this.particle = particle;
        this.chance = chance;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(chance) == 0) {
            InfinityFx.blockSparkles(level, pos, random, particle.get(), 1);
        }
    }
}
