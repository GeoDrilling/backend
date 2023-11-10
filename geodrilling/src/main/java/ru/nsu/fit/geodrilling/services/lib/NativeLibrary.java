package ru.nsu.fit.geodrilling.services.lib;

import ru.nsu.fit.geodrilling.dto.InputAreasEquivalence;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.InputModelSignal;
import ru.nsu.fit.geodrilling.model.AreasEquivalence;
import ru.nsu.fit.geodrilling.model.ModelSignal;
import ru.nsu.fit.geodrilling.model.OutputModel;


public class NativeLibrary {
  static {
    System.loadLibrary("native");
  }
  public native OutputModel startModelSimpleHorizontalModel6Param1_5D_ByRo(
      int nprobes, int[] num_probe,
      int npoints, double[] md, double[] tvd, double[] x, double[] zeni,
      double[] ro_by_phases, double[] ro_by_ampl,
      double tvd_start, double min_tvd_start, double max_tvd_start,
      double alpha, double min_alpha, double max_alpha,
      double ro_up, double kanisotropy_up,
      double ro_down, double kanisotropy_down
  );
  public OutputModel startModel(InputBuildModel model) {
    return startModelSimpleHorizontalModel6Param1_5D_ByRo(
        model.nprobes, model.num_probe,
        model.npoints, model.md, model.tvd, model.x, model.zeni,
        model.ro_by_phases, model.ro_by_ampl,
        model.tvd_start, model.min_tvd_start, model.max_tvd_start,
        model.alpha, model.min_alpha, model.max_alpha,
        model.ro_up, model.kanisotropy_up,
        model.ro_down, model.kanisotropy_down
        );
  }

  public native OutputModel SolverHorizontalModel6Param1_5DByRo(
      int nprobes, int[] num_probe,
      int npoints, double[] md, double[] tvd, double[] x, double[] zeni,
      double[] ro_by_phases, double[] ro_by_ampl,
      double tvd_start, double min_tvd_start, double max_tvd_start,
      double alpha, double min_alpha, double max_alpha,
      double ro_up, double kanisotropy_up,
      double ro_down, double kanisotropy_down
  );
  public OutputModel solverModel(InputBuildModel model) {
    return SolverHorizontalModel6Param1_5DByRo(
        model.nprobes, model.num_probe,
        model.npoints, model.md, model.tvd, model.x, model.zeni,
        model.ro_by_phases, model.ro_by_ampl,
        model.tvd_start, model.min_tvd_start, model.max_tvd_start,
        model.alpha, model.min_alpha, model.max_alpha,
        model.ro_up, model.kanisotropy_up,
        model.ro_down, model.kanisotropy_down
    );
  }

  public native ModelSignal LoggingHorizontalModel6Param1_5DRo(
      int nprobes, int[] num_probe,
      int npoints, double[] tvd, double[] x, double[] zeni,
      double tvd_start, double alpha, double ro_up, double kanisotropy_up,
      double ro_down, double kanisotropy_down
  );

  public ModelSignal simulateModelSignal(InputModelSignal model) {
    return LoggingHorizontalModel6Param1_5DRo(
        model.nprobes, model.num_probe,
        model.npoints, model.tvd, model.x, model.zeni,
        model.tvd_start, model.alpha, model.ro_up, model.kanisotropy_up,
        model.ro_down, model.kanisotropy_down
    );
  }

  public native AreasEquivalence TargetFunctions(
      int nprobes, int[] num_probe,
      int npoints, double[] tvd, double[] x, double[] zeni,
      int n_tvd_start, double[] tvd_start, int n_alpha,
      double[] alpha, int n_ro_up, double[] ro_up,
      int n_kanisotropy_up, double[] kanisotropy_up,
      int n_ro_down, double[] ro_down, int n_kanisotropy_down,
      double[] kanisotropy_down, double[] phases,
      double[] ampls
  );
  public AreasEquivalence createAreasEquivalence(InputAreasEquivalence model) {
    return TargetFunctions(
        model.nprobes, model.num_probe,
        model.npoints, model.tvd, model.x, model.zeni,
        model.n_tvd_start, model.tvd_start, model.n_alpha,
        model.alpha, model.n_ro_up, model.ro_up,
        model.n_kanisotropy_up, model.kanisotropy_up,
        model.n_ro_down, model.ro_down, model.n_kanisotropy_down,
        model.kanisotropy_down, model.phases,
        model.ampls
    );
  }
}
