package com.mcecraft.noiseterrain;

import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class NoiseTerrainGenerator3D implements Generator {
    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.subdivide().forEach(this::generateSection);
    }

    void generateSection(@NotNull GenerationUnit unit) {

    }
}
