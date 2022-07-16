package com.mcecraft.noiseterrain.math;

public class MathUtils {
    public static double lerp(double alpha, double val1, double val2) {
        return (1 - alpha) * val1 + alpha * val2;
    }

    public static float lerp(float alpha, float val1, float val2) {
        return (1 - alpha) * val1 + alpha * val2;
    }

    public static double clamp(double val, double min, double max) {
        return Math.min(Math.max(val, min), max);
    }

    public static float clamp(float val, float min, float max) {
        return Math.min(Math.max(val, min), max);
    }

    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static float map(float x, float inMin, float inMax, float outMin, float outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
