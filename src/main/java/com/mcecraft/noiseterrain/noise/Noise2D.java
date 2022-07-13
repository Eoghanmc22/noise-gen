package com.mcecraft.noiseterrain.noise;

import com.mcecraft.noiseterrain.math.MathUtils;
import org.jetbrains.annotations.NotNull;

public interface Noise2D {
    float get(double x, double y);


    Noise2D ZERO = (x, y) -> 0f;

    static Noise2D constant(float constant) {
        return (x, y) -> constant;
    }

    static Noise2D scale(@NotNull Noise2D noise, Noise2D factor) {
        return (x, y) -> {
            double f = factor.get(x, y);
            return noise.get(x * f, y * f);
        };
    }

    static Noise2D scale(@NotNull Noise2D noise, Noise2D factorX, Noise2D factorY) {
        return (x, y) -> noise.get(x * factorX.get(x, y), y * factorY.get(x, y));
    }

    static Noise2D abs(@NotNull Noise2D noise) {
        return (x, y) -> Math.abs(noise.get(x, y));
    }

    static Noise2D square(@NotNull Noise2D noise) {
        return (x, y) -> {
            float v = noise.get(x, y);
            return v * v;
        };
    }

    static Noise2D cube(@NotNull Noise2D noise) {
        return (x, y) -> {
            float v = noise.get(x, y);
            return v * v * v;
        };
    }

    static Noise2D min(@NotNull Noise2D noise1, @NotNull Noise2D noise2) {
        return (x, y) -> Math.min(noise1.get(x, y), noise2.get(x, y));
    }

    static Noise2D max(@NotNull Noise2D noise1, @NotNull Noise2D noise2) {
        return (x, y) -> Math.max(noise1.get(x, y), noise2.get(x, y));
    }

    static Noise2D clamp(@NotNull Noise2D noise, @NotNull Noise2D min, @NotNull Noise2D max) {
        return (x, y) -> MathUtils.clamp(noise.get(x, y), min.get(x, y), max.get(x, y));
    }

    static Noise2D mul(@NotNull Noise2D noise1, @NotNull Noise2D noise2) {
        return (x, y) -> noise1.get(x, y) * noise2.get(x, y);
    }

    static Noise2D add(@NotNull Noise2D noise1, @NotNull Noise2D noise2) {
        return (x, y) -> noise1.get(x, y) + noise2.get(x, y);
    }


    static Noise2D noise(@NotNull FastNoiseLite noise) {
        return noise::getNoise;
    }
}
