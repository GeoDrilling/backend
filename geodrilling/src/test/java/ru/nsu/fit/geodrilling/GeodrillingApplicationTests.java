package ru.nsu.fit.geodrilling;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.geodrilling.model.*;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

import static ru.nsu.fit.geodrilling.model.Constant.NAN;


//@SpringBootTest
class GeodrillingApplicationTests {
    final int npoints = 3;
    final int nprobes = 2;
    final int[] num_probe = {Constant.LWD_LOOCH_VIKPB_ROL, Constant.LWD_LOOCH_VIKPB_ROH};
    @Test
    void testLoggingHorizontal() {

        double[] tvd = {2, 2, 2};
        double[] x = {0, 1, 2};
        double[] zeni = {0, 0, 0};
        double tvdstart = 1.5;
        double alpha = 0;
        double ro_up = 10;
        double ro_down = 10;
        double kanisotropy_up = 1;
        double kanisotrypy_down = 1;
        NativeLibrary lib = new NativeLibrary();

        ModelSignal modelSignal = lib.LoggingHorizontalModel6Param1_5DRo(
                nprobes, num_probe, npoints, tvd, x, zeni, tvdstart, alpha, ro_up, kanisotropy_up, ro_down, kanisotrypy_down
        );
        System.out.println(modelSignal);
    }

    @Test
    void testTargetFunction() {
        int npoints = 3;
        double[] tvd = {2, 2, 2};
        double[] x = {0, 1, 2};
        double[] zeni = {0, 0, 0};

        int n_tvd_start = 2;
        double[] tvdstart = {1.5, 2};

        int n_alpha = 4;
        double[] alpha = {0, 10, 20, 30};

        int n_ro_up = 2;
        double[] ro_up = {10, 15};

        int n_ro_down = 3;
        double[] ro_down = {10, 20, 30};

        int n_kanisotropy_up = 1;
        double[] kanisotropy_up = {1};

        int n_kanisotropy_down = 2;
        double[] kanisotropy_down = {1, 1.1};

        double[] ro_by_phases = {10.3293, 10.7292, 10.3293, 10.7292, 10.3293, 10.7292};
        double[] ro_by_ampls = {9.19793, 8.49338, 9.19793, 8.49338, 9.19793, 8.49338};

        NativeLibrary lib = new NativeLibrary();
        AreasEquivalence areasEquivalence = lib.TargetFunctions(
                nprobes, num_probe,
                npoints, tvd, x, zeni,
                n_tvd_start, tvdstart,
                n_alpha, alpha,
                n_ro_up, ro_up,
                n_kanisotropy_up, kanisotropy_up,
                n_ro_down, ro_down,
                n_kanisotropy_down, kanisotropy_down,
                ro_by_phases, ro_by_ampls
        );
        System.out.println(areasEquivalence);
    }
    @Test
    void testSolver() {
        int npoints = 3;
        double[] tvd = { 2, 2, 2 };
        double[] x = { 0, 1, 2 };
        double[] zeni = { 0, 0, 0 };
        double tvdstart = 1.5;
        double alpha = 0;
        double ro_up = 10;
        double ro_down = 10;
        double kanisotropy_up = 1;
        double kanisotrypy_down = 1;

        double[] ro_by_phases = { 10.3293, 10.7292, 10.3293, 10.7292, 10.3293, 10.7292 };
        double[] ro_by_ampls = { 9.19793, 8.49338, 9.19793, 8.49338, 9.19793, 8.49338 };

        NativeLibrary lib = new NativeLibrary();

        OutputModel model = lib.SolverHorizontalModel6Param1_5DByRo(
                nprobes, num_probe, npoints, tvd, tvd, x, zeni,
                ro_by_phases, ro_by_ampls,
                tvdstart, NAN, NAN,
                alpha, NAN, NAN,
                ro_up, kanisotropy_up, ro_down, kanisotrypy_down
        );
        System.out.println(model);
    }
    @Test
    void testStartModel() {
        double[] tvd = { 2, 2, 2 };
        double[] x = { 0, 1, 2 };
        double[] zeni = { 0, 0, 0 };

        double[] ro_by_phases = { 10.3293, 10.7292, 10.3293, 10.7292, 10.3293, 10.7292 };
        double[] ro_by_ampls = { 9.19793, 8.49338, 9.19793, 8.49338, 9.19793, 8.49338 };

        NativeLibrary lib = new NativeLibrary();
        OutputModel model = lib.startModelSimpleHorizontalModel6Param1_5D_ByRo(
                nprobes, num_probe, npoints, tvd, tvd, x, zeni,
                ro_by_phases, ro_by_ampls,
                NAN, NAN, NAN,
                NAN, NAN, NAN, NAN, NAN, NAN, NAN
        );
        System.out.println(model);
    }


}
