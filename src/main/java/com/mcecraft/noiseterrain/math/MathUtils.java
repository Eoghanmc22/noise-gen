package com.mcecraft.noiseterrain.math;

public class MathUtils {
    public static double lerp(double val1, double val2, double alpha) {
        return (1 - alpha) * val1 + alpha * val2;
    }

    public static float lerp(float val1, float val2, float alpha) {
        return (1 - alpha) * val1 + alpha * val2;
    }

    public static double clamp(double val, double min, double max) {
        return Math.min(Math.max(val, min), max);
    }

    public static float clamp(float val, float min, float max) {
        return Math.min(Math.max(val, min), max);
    }
}
