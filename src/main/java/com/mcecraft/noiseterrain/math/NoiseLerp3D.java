package com.mcecraft.noiseterrain.math;

import com.mcecraft.noiseterrain.noise.Noise3D;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Chunk;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

public class NoiseLerp3D implements Noise3D {

    private final Point position;
    // Current planes
    // cellX * cellsXYZ * cellsXYZ + cellZ * cellsXYZ + cellY
    private final float[] noiseCache;

    private final int cellsXYZ;
    private final int cellSizeXYZ;

    public NoiseLerp3D(@NotNull Noise3D noiseMap, @NotNull Point position, int cellsXYZ) {
        Check.argCondition(Chunk.CHUNK_SIZE_X % cellsXYZ == 0, "Chunk size must be divisible by cellsXYZ");

        this.position = position;
        this.cellsXYZ = cellsXYZ + 1;
        this.cellSizeXYZ = Chunk.CHUNK_SIZE_X / cellsXYZ;
        this.noiseCache = new float[this.cellsXYZ * this.cellsXYZ * this.cellsXYZ];

        fill(noiseMap);
    }

    private int getIdx(int cellX, int cellY, int cellZ) {
        Check.argCondition(withinCache(cellX, cellY, cellZ, this.cellsXYZ), "Attempted to get noise cell outside of cache");
        return cellX * this.cellsXYZ * this.cellsXYZ + cellZ * this.cellsXYZ + cellY;
    }

    private void fill(Noise3D noiseMap) {
        double x = position.x();
        double y = position.x();
        double z = position.x();

        for (int cellX = 0; cellX < cellsXYZ; cellX++) {
            for (int cellZ = 0; cellZ < cellsXYZ; cellZ++) {
                for (int cellY = 0; cellY < cellsXYZ; cellY++) {
                    noiseCache[getIdx(cellX, cellY, cellZ)] = noiseMap.get(
                            x + cellX * cellSizeXYZ,
                            y + cellY * cellSizeXYZ,
                            z + cellZ * cellSizeXYZ
                    );
                }
            }
        }
    }

    @Override
    public float get(double x, double y, double z) {
        Check.argCondition(sameSection(this.position, x, y, z), "Attempted to evaluate noise outside of section");

        double relX = x - this.position.x();
        double relY = y - this.position.y();
        double relZ = z - this.position.z();

        int cellX = Math.floorDiv((int) Math.floor(relX), this.cellSizeXYZ);
        int cellY = Math.floorDiv((int) Math.floor(relY), this.cellSizeXYZ);
        int cellZ = Math.floorDiv((int) Math.floor(relZ), this.cellSizeXYZ);

        float noise000 = this.noiseCache[getIdx(cellX + 0, cellY + 0, cellZ + 0)];
        float noise010 = this.noiseCache[getIdx(cellX + 0, cellY + 1, cellZ + 0)];
        float noise001 = this.noiseCache[getIdx(cellX + 0, cellY + 0, cellZ + 1)];
        float noise011 = this.noiseCache[getIdx(cellX + 0, cellY + 1, cellZ + 1)];
        float noise100 = this.noiseCache[getIdx(cellX + 1, cellY + 0, cellZ + 0)];
        float noise110 = this.noiseCache[getIdx(cellX + 1, cellY + 1, cellZ + 0)];
        float noise101 = this.noiseCache[getIdx(cellX + 1, cellY + 0, cellZ + 1)];
        float noise111 = this.noiseCache[getIdx(cellX + 1, cellY + 1, cellZ + 1)];

        float fractX = ((float) relX - cellX * this.cellSizeXYZ) / this.cellSizeXYZ;
        float fractY = ((float) relY - cellY * this.cellSizeXYZ) / this.cellSizeXYZ;
        float fractZ = ((float) relZ - cellZ * this.cellSizeXYZ) / this.cellSizeXYZ;

        return MathUtils.lerp(
                fractZ,
                MathUtils.lerp(
                        fractX,
                        MathUtils.lerp(fractY, noise000, noise010),
                        MathUtils.lerp(fractY, noise100, noise110)
                ),
                MathUtils.lerp(
                        fractX,
                        MathUtils.lerp(fractY, noise001, noise011),
                        MathUtils.lerp(fractY, noise101, noise111)
                )
        );
    }

    private static boolean sameSection(Point position, double x, double y, double z) {
        return ChunkUtils.getChunkCoordinate(x) == position.chunkX() &&
                ChunkUtils.getChunkCoordinate(y) == position.section() &&
                ChunkUtils.getChunkCoordinate(z) == position.chunkZ();
    }

    private static boolean withinCache(int cellX, int cellY, int cellZ, int cellsXYZ) {
        return cellX >= 0 && cellX < cellsXYZ &&
                cellY >= 0 && cellY < cellsXYZ &&
                cellZ >= 0 && cellZ < cellsXYZ;
    }
}
