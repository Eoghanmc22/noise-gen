package com.mcecraft.noiseterrain.noise;

import com.mcecraft.noiseterrain.math.MathUtils;
import org.jetbrains.annotations.NotNull;

public interface Noise3D {
    float get(double x, double y, double z);


    Noise3D ZERO = (x, y, z) -> 0f;

    static Noise3D constant(float constant) {
        return (x, y, z) -> constant;
    }

    static Noise3D scale(@NotNull Noise3D noise, Noise3D factor) {
        return (x, y, z) -> {
            double f = factor.get(x, y, z);
            return noise.get(x * f, y * f, z * f);
        };
    }

    static Noise3D scale(@NotNull Noise3D noise, Noise3D factorXZ, Noise3D factorY) {
        return (x, y, z) -> {
            double fXZ = factorXZ.get(x, y, z);
            double fY = factorY.get(x, y, z);
            return noise.get(x * fXZ, y * fY, z * fXZ);
        };
    }

    static Noise3D abs(@NotNull Noise3D noise) {
        return (x, y, z) -> Math.abs(noise.get(x, y, z));
    }

    static Noise3D square(@NotNull Noise3D noise) {
        return (x, y, z) -> {
            float v = noise.get(x, y, z);
            return v * v;
        };
    }

    static Noise3D cube(@NotNull Noise3D noise) {
        return (x, y, z) -> {
            float v = noise.get(x, y, z);
            return v * v * v;
        };
    }

    static Noise3D min(@NotNull Noise3D noise1, @NotNull Noise3D noise2) {
        return (x, y, z) -> Math.min(noise1.get(x, y, z), noise2.get(x, y, z));
    }

    static Noise3D max(@NotNull Noise3D noise1, @NotNull Noise3D noise2) {
        return (x, y, z) -> Math.max(noise1.get(x, y, z), noise2.get(x, y, z));
    }

    static Noise3D clamp(@NotNull Noise3D noise, @NotNull Noise3D min, @NotNull Noise3D max) {
        return (x, y, z) -> MathUtils.clamp(noise.get(x, y, z), min.get(x, y, z), max.get(x, y, z));
    }

    static Noise3D mul(@NotNull Noise3D noise1, @NotNull Noise3D noise2) {
        return (x, y, z) -> noise1.get(x, y, z) * noise2.get(x, y, z);
    }

    static Noise3D add(@NotNull Noise3D noise1, @NotNull Noise3D noise2) {
        return (x, y, z) -> noise1.get(x, y, z) + noise2.get(x, y, z);
    }

    static Noise3D choose(@NotNull Noise3D selector, float rangeMin, float rangeMax, @NotNull Noise3D inRange, @NotNull Noise3D outOfRange) {
        return (x, y, z) -> {
            float s = selector.get(x, y, z);
            return s >= rangeMin && s < rangeMax ? inRange.get(x, y, z) : outOfRange.get(x, y, z);
        };
    }

    static Noise3D map(@NotNull Noise3D noise, float inMin, float inMax, float outMin, float outMax) {
        return (x, y, z) -> MathUtils.map(noise.get(x, y, z), inMin, inMax, outMin, outMax);
    }

    static Noise3D surface(@NotNull Noise3D noise, @NotNull Noise3D offset) {
        return (x, y, z) -> noise.get(x, offset.get(x, 0, z), z);
    }

    static Noise3D distort(@NotNull Noise3D noise, @NotNull Noise3D distort) {
        return (x, y, z) -> noise.get(x + distort.get(x, y, z), y, z + distort.get(z, y, x));
    }


    static Noise3D noise(@NotNull FastNoiseLite noise) {
        return noise::getNoise;
    }

    static Noise3D noise(@NotNull FastNoiseLite noise, @NotNull FastNoiseLite turbulence) {
        return (x, y, z) -> {
            FastNoiseLite.Vector3 coord = new FastNoiseLite.Vector3(x, y, z);
            turbulence.domainWarp(coord);
            return noise.getNoise(coord.x, coord.y, coord.z);
        };
    }

    static Noise3D clampedGradient(double startY, double endY, double startVal, double endVal) {
        return (x, y, z) -> {
            double gradient = (y - startY) / (endY - startY);
            if (endVal > startVal) {
                return (float) MathUtils.clamp(MathUtils.lerp(gradient, startVal, endVal), startVal, endVal);
            } else {
                return (float) MathUtils.clamp(MathUtils.lerp(gradient, startVal, endVal), endVal, startVal);
            }
        };
    }
}
