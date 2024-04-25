package ru.nsu.fit.geodrilling.services;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterpolationService {

    public Map<Double, Double[]> interpolateDepths(
            double[] depths1, double[] values1, double[] depths2, double[] values2) {

        TreeSet<Double> allDepths = new TreeSet<>();

        for (double depth : depths1) {
            allDepths.add(depth);
        }

        for (double depth : depths2) {
            allDepths.add(depth);
        }

        double minDepth = Math.max(Arrays.stream(depths1).min().orElse(Double.NaN),
                Arrays.stream(depths2).min().orElse(Double.NaN));

        double maxDepth = Math.min(Arrays.stream(depths1).max().orElse(Double.NaN),
                Arrays.stream(depths2).max().orElse(Double.NaN));

        Map<Double, Double[]> interpolatedResults = new LinkedHashMap<>();

        for (double depth : allDepths) {
            if (depth >= minDepth && depth <= maxDepth) {
                interpolatedResults.put(depth, new Double[]{
                        interpolatePoint(depths1, values1, depth),
                        interpolatePoint(depths2, values2, depth)
                });
            }
        }

        return interpolatedResults;
    }

    private double interpolatePoint(double[] depths, double[] values, double point) {

        if (depths.length < 2) {
            throw new IllegalArgumentException("Not enough data points for interpolation.");
        }

        LinearInterpolator interpolator = new LinearInterpolator();
        PolynomialSplineFunction function = interpolator.interpolate(depths, values);

        if (point < depths[0] || point > depths[depths.length - 1]) {
            return Double.NaN;
        }

        return function.value(point);
    }
}
