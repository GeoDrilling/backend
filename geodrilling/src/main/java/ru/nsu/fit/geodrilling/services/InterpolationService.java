package ru.nsu.fit.geodrilling.services;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;

import java.util.*;


@Service
public class InterpolationService {

    public static List<Double> convertToFloatList(double[] doubleArray) {
        List<Double> floatList = new ArrayList<>();

        for (double value : doubleArray) {
            floatList.add((double) value);
        }

        return floatList;
    }
    public InterpolateDTO interpolateDepths(
            double[] depths1, List<double[]> values1, double[] depths2, List<double[]> values2) {

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

        InterpolateDTO interpolateDTO = new InterpolateDTO();
        boolean i = true;
        int j = 0;
        for (double depth : allDepths) {
            if (depth >= minDepth && depth <= maxDepth) {
                interpolateDTO.depth.add(depth);
                for (double[] v1 : values1) {
                    if (i) {
                        interpolateDTO.curves.add(new ArrayList<>());
                    }
                    interpolateDTO.curves.get(j).add(interpolatePoint(depths1, v1, depth));
                    j++;
                }
                for (double[] v2 : values2) {
                    if (i) {
                        interpolateDTO.curves.add(new ArrayList<>());
                    }
                    interpolateDTO.curves.get(j).add(interpolatePoint(depths2, v2, depth));
                    j++;
                }
                j = 0;
                i = false;
            }
        }

        return interpolateDTO;
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
