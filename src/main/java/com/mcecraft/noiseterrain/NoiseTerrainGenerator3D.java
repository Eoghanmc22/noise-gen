package com.mcecraft.noiseterrain;

import com.mcecraft.noiseterrain.math.NoiseLerp3D;
import com.mcecraft.noiseterrain.mojang.MojNoise;
import com.mcecraft.noiseterrain.noise.Noise3D;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NoiseTerrainGenerator3D implements Generator {

    private static final int EXPECTED_SIZE = 16;

    final Noise3D density;

    public NoiseTerrainGenerator3D(int seed) {
        // Currently using mojang code verbatim, cant open source
        density = MojNoise.slopedCheese(seed);

        // prob not needed
        if (Chunk.CHUNK_SIZE_X != EXPECTED_SIZE || Chunk.CHUNK_SECTION_SIZE != EXPECTED_SIZE || Chunk.CHUNK_SIZE_Z != EXPECTED_SIZE) {
            throw new UnsupportedOperationException("Unit is not the size of a section");
        }
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.subdivide().forEach(this::generateSection);
    }

    void generateSection(@NotNull GenerationUnit unit) {
        if (!Objects.equals(unit.size(), new Vec(Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SECTION_SIZE, Chunk.CHUNK_SIZE_Z))) {
            throw new UnsupportedOperationException("Unit is not the size of a section");
        }

        int offX = unit.absoluteStart().blockX();
        int offY = unit.absoluteStart().blockY();
        int offZ = unit.absoluteStart().blockZ();

        NoiseLerp3D densityInterpolator = new NoiseLerp3D(density, unit.absoluteStart(), 4);
        unit.modifier().setAllRelative((x, y, z) -> {
            float density = densityInterpolator.get(x + offX, y + offY, z + offZ);

            if (density > 0f) {
                return Block.STONE;
            } else {
                if (y + offY > 64) {
                    return Block.AIR;
                } else {
                    return Block.WATER;
                }
            }
        });
    }
}
