package ru.nsu.fit.geodrilling.services;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.CurveDto;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


@Service
public class InterpolationService {

    public static List<Double> convertToDoubleList(Double[] doubleArray) {
        List<Double> floatList = new ArrayList<>();

        for (double value : doubleArray) {
            floatList.add((double) value);
        }

        return floatList;
    }

    private double[] listDoubleInDoubleArray(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }
    /*public InterpolateDTO interpolateDepths(
            double[] depths1, List<double[]> values1, double[] depths2, List<double[]> values2) {

        TreeSet<Double> allDepths = new TreeSet<>();

        for (double depth : depths1) {
            allDepths.add(depth);
        }

        for (double depth : depths2) {
            allDepths.add(depth);
        }

        double minDepth1 = Math.max(Arrays.stream(depths1).min().orElse(NaN),
                Arrays.stream(depths2).min().orElse(NaN));

        double maxDepth1 = Math.min(Arrays.stream(depths1).max().orElse(NaN));

        InterpolateDTO interpolateDTO = new InterpolateDTO();
        boolean i = true;
        int j = 0;
        for (double depth : allDepths) {
            interpolateDTO.depth.add(depth);
            if (depth >= minDepth && depth <= maxDepth) {
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
            if (depth < minDepth || depth > maxDepth) {
                for (double[] v1 : values1) {
                    if (i) {
                        interpolateDTO.curves.add(new ArrayList<>());
                    }
                    interpolateDTO.curves.get(j).add(null);
                    j++;
                }
                for (double[] v2 : values2) {
                    if (i) {
                        interpolateDTO.curves.add(new ArrayList<>());
                    }
                    interpolateDTO.curves.get(j).add(null);
                    j++;
                }
                j = 0;
                i = false;
            }
        }

        return interpolateDTO;
    }*/

    public InterpolateDTO interpolateDepths(
            double[] depths1, List<double[]> values1, double[] depths2, List<double[]> values2) {

        TreeSet<Double> allDepthsSet = new TreeSet<>();

        for (double depth : depths1) {
            allDepthsSet.add(depth);
        }

        for (double depth : depths2) {
            allDepthsSet.add(depth);
        }

        List<Double> allDepthList = new ArrayList<>(allDepthsSet);
        double[] allDepth = listDoubleInDoubleArray(allDepthList);
        InterpolateDTO interpolateDTO = new InterpolateDTO();
        interpolateDTO.depth = allDepthList;
        for (double[] v1 : values1) {
            interpolateDTO.curves.add(interpolateNull(depths1, v1, allDepth));
        }
        for (double[] v2 : values2) {
            interpolateDTO.curves.add(interpolateNull(depths2, v2, allDepth));
        }


        return interpolateDTO;
    }
    public List<Double> extrapolateCurves(
            Double[] curve, Double[] depth, boolean x, boolean tvd, Double[] zeni) {
        int indexNotNull = -1;
        int indexMinZeni = -1;
        int indexMaxZeni = -1;
        if (x) {
            for (int i = 0; indexMinZeni == -1; i++) {
                if ((zeni[i]) == null) {
                }
                else {
                    indexMinZeni = i;
                }
            }
            for (int i = indexMinZeni; indexMaxZeni == -1; i++) {
                if (i == zeni.length - 1) {
                    indexMaxZeni = i;
                }
                if ((zeni[i]) == null) {
                    indexMaxZeni = i - 1;
                }
            }
            for (int i = 0; indexNotNull == -1; i++) {
                if ((curve[i]) == null) {
                }
                else {
                    indexNotNull = i;
                }
            }
            for (int i = indexNotNull; i >= 0; i--) {
                if ((curve[i]) == null)
                    curve[i] = -sin(zeni[indexMinZeni]) * (depth[i + 2] - depth[i + 1]) + curve[i + 1];
            }
            for (int i = indexNotNull; i < curve.length; i++) {
                if ((curve[i]) == null) {
                    curve[i] = sin(zeni[indexMaxZeni]) * (depth[i - 1] - depth[i - 2]) + curve[i - 1];
                }
            }
            return convertToDoubleList(curve);
        } else if (tvd) {
            for (int i = 0; indexMinZeni == -1; i++) {
                if ((zeni[i]) == null) {
                }
                else {
                    indexMinZeni = i;
                }
            }
            for (int i = indexMinZeni; indexMaxZeni == -1; i++) {
                if (i == zeni.length - 1) {
                    indexMaxZeni = i;
                }
                if ((zeni[i]) == null) {
                    indexMaxZeni = i - 1;
                }
            }
            for (int i = 0; indexNotNull == -1; i++) {
                if ((curve[i]) == null) {
                }
                else {
                    indexNotNull = i;
                }
            }
            for (int i = indexNotNull; i >= 0; i--) {
                if ((curve[i]) == null)
                    curve[i] = -cos(zeni[indexMinZeni]) * (depth[i + 2] - depth[i + 1]) + curve[i + 1];
            }
            for (int i = indexNotNull; i < curve.length; i++) {
                if ((curve[i]) == null) {
                    curve[i] = cos(zeni[indexMaxZeni]) * (depth[i - 1] - depth[i - 2]) + curve[i - 1];
                }
            }
            return convertToDoubleList(curve);
        } else {
            for (int i = 0; indexNotNull == -1; i++) {
                if ((curve[i]) == null) {
                }
                else {
                    indexNotNull = i;
                }
            }
            for (int i = indexNotNull; i >= 0; i--) {
                if ((curve[i]) == null)
                    curve[i] = curve[i + 1];
            }
            for (int i = indexNotNull; i < curve.length; i++) {
                if ((curve[i]) == null) {
                    curve[i] = curve[i - 1];
                }
            }
            return convertToDoubleList(curve);
        }
    }

    public List<Double> interpolateSynthetic(double[] depths1, double[] values1, double[] depths2){
        double minDepth = Math.max(Arrays.stream(depths1).min().orElse(NaN),
                Arrays.stream(depths2).min().orElse(NaN));

        double maxDepth = Math.min(Arrays.stream(depths1).max().orElse(NaN),
                Arrays.stream(depths2).max().orElse(NaN));
        List<Double> curveInterpolate = new ArrayList<>();
        for (double depth : depths2) {
            if (depth >= minDepth && depth <= maxDepth) {
                curveInterpolate.add(interpolatePoint(depths1, values1, depth));

            }
            if (depth < minDepth) {
                curveInterpolate.add(values1[0]);
            }
            if (depth > maxDepth) {
                curveInterpolate.add(values1[values1.length - 1]);
            }
        }
        return curveInterpolate;
    }

    public List<Double> interpolateNull(double[] depths1, double[] values1, double[] depths2){
        double minDepth = Math.max(Arrays.stream(depths1).min().orElse(NaN),
                Arrays.stream(depths2).min().orElse(NaN));

        double maxDepth = Math.min(Arrays.stream(depths1).max().orElse(NaN),
                Arrays.stream(depths2).max().orElse(NaN));
        List<Double> curveInterpolate = new ArrayList<>();
        for (double depth : depths2) {
            if (depth >= minDepth && depth <= maxDepth) {
                curveInterpolate.add(interpolatePoint(depths1, values1, depth));

            }
            if (depth < minDepth) {
                curveInterpolate.add(null);
            }
            if (depth > maxDepth) {
                curveInterpolate.add(null);
            }
        }
        return curveInterpolate;
    }

    private double interpolatePoint(double[] depths, double[] values, double point) {

        if (depths.length < 2) {
            throw new IllegalArgumentException("Not enough data points for interpolation.");
        }

        LinearInterpolator interpolator = new LinearInterpolator();
        PolynomialSplineFunction function = interpolator.interpolate(depths, values);

        if (point < depths[0] || point > depths[depths.length - 1]) {
            return NaN;
        }

        return function.value(point);
    }
}
