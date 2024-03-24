package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.InputAreasEquivalence;
import ru.nsu.fit.geodrilling.dto.InputParamAreasDTO;
import ru.nsu.fit.geodrilling.entity.AreasEntity;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.model.AreasEquivalence;
import ru.nsu.fit.geodrilling.repositories.ModelRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.drawingAreasEquivalent.ColorMapBuilder;
import ru.nsu.fit.geodrilling.services.drawingAreasEquivalent.PythonService;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.nsu.fit.geodrilling.model.Constant.NAN;



@Service
@AllArgsConstructor
public class AreasService {
    private ModelRepository modelRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CurvesService lasFileService;
    private final NativeLibrary nativeLibrary;
    private final PythonService pythonService;
    private double[] ListDoubleInDoubleArray(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static double findMax(double[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Массив не должен быть пустым");
        }

        double max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    public static List<Float> convertToFloatList(double[] doubleArray) {
        List<Float> floatList = new ArrayList<>();

        for (double value : doubleArray) {
            floatList.add((float) value);
        }

        return floatList;
    }

    private static double findMin(double[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Массив не должен быть пустым");
        }

        double min = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    public ByteArrayResource createAreas(Long idModel, InputParamAreasDTO inputParamAreasDTO) {
        AreasEntity areasEntity = new AreasEntity();
        ModelEntity modelEntity = modelRepository.findById(idModel).orElseThrow(() -> new EntityNotFoundException("Модель не найдена"));
        ProjectEntity projectEntity = modelEntity.getProjectEntity();
        Long idProject = projectEntity.getId();


        List<String> curves = new ArrayList<>(lasFileService.getCurvesNames(idProject).getCurvesNames());
        boolean bolPL = false;
        boolean bolPLD = false;
        boolean bolPLE = false;
        boolean bolPH = false;
        boolean bolPHD = false;
        boolean bolPHE = false;
        boolean bolAL = false;
        boolean bolALD = false;
        boolean bolALE = false;
        boolean bolAH = false;
        boolean bolAHD = false;
        boolean bolAHE = false;
        int nprobes = 0;
        int[] num_probe = new int[6];
        double tvd_start = NAN;
        double min_tvd_start = NAN;
        double max_tvd_start = NAN;
        double alpha = NAN;
        double min_alpha = NAN;
        double max_alpha = NAN;
        double ro_up = NAN;
        double kanisotropy_up = NAN;
        double ro_down = NAN;
        double kanisotropy_down = NAN;
        double[] arrPL = null;
        double[] arrPLD = null;
        double[] arrPLE = null;
        double[] arrPH = null;
        double[] arrPHD = null;
        double[] arrPHE = null;
        double[] arrAL = null;
        double[] arrALD = null;
        double[] arrALE = null;
        double[] arrAH = null;
        double[] arrAHD = null;
        double[] arrAHE = null;
        String ROPL = projectEntity.getSootEntity().getROPL();
        String ROPLD = projectEntity.getSootEntity().getROPLD();
        String ROPLE = projectEntity.getSootEntity().getROPLE();
        String ROPH = projectEntity.getSootEntity().getROPH();
        String ROPHD = projectEntity.getSootEntity().getROPHD();
        String ROPHE = projectEntity.getSootEntity().getROPHE();
        String ROAL = projectEntity.getSootEntity().getROAL();
        String ROALD = projectEntity.getSootEntity().getROALD();
        String ROALE = projectEntity.getSootEntity().getROALE();
        String ROAH = projectEntity.getSootEntity().getROAH();
        String ROAHD = projectEntity.getSootEntity().getROAHD();
        String ROAHE = projectEntity.getSootEntity().getROAHE();
        String md = projectEntity.getSootEntity().getMd();
        String tvd = projectEntity.getSootEntity().getTvd();
        String x = projectEntity.getSootEntity().getX();
        String zeni = projectEntity.getSootEntity().getZeni();
        int length = 0;
        if (curves.contains(ROPL) || curves.contains(ROAL)) {
            if (curves.contains(ROPL)) {
                arrPL = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(ROPL, idProject).getCurveData());
                bolPL = true;
                length = arrPL.length;
            }
            if (curves.contains(ROAL)) {
                arrAL = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(ROAL, idProject).getCurveData());
                bolAL = true;
                length = arrAL.length;
            }
            num_probe[nprobes] = 1039;
            nprobes += 1;
        }
        /*if (curves.contains(ROPLD) || curves.contains(ROALD)) {
            if (curves.contains(ROPLD)) {
                arrPLD = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROPLD));
                bolPLD = true;
                length = arrPLD.length;
            }
            if (curves.contains(ROALD)) {
                arrALD = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROALD));
                bolALD = true;
                length = arrALD.length;
            }
            num_probe[nprobes] = 1040;
            nprobes += 1;
        }
        if (curves.contains(ROPLE) || curves.contains(ROALE)) {
            if (curves.contains(ROPLE)) {
                arrPLE = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROPLE));
                bolPLE = true;
                length = arrPLE.length;
            }
            if (curves.contains(ROALE)) {
                arrALE = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROALE));
                bolALE = true;
                length = arrALE.length;
            }
            num_probe[nprobes] = 1041;
            nprobes += 1;
        }*/
        if (curves.contains(ROPH) || curves.contains(ROAH)) {
            if (curves.contains(ROPH)) {
                arrPH = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(ROPH, idProject).getCurveData());
                bolPH = true;
                length = arrPH.length;
            }
            if (curves.contains(ROAH)) {
                arrAH = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(ROAH, idProject).getCurveData());
                bolAH = true;
                length = arrAH.length;
            }
            num_probe[nprobes] = 1042;
            nprobes += 1;
        }
        /*if (curves.contains(ROPHD) || curves.contains(ROAHD)) {
            if (curves.contains(ROPHD)) {
                arrPHD = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROPHD));
                bolPHD = true;
                length = arrPHD.length;
            }
            if (curves.contains(ROAHD)) {
                arrAHD = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROAHD));
                bolAHD = true;
                length = arrAHD.length;
            }
            num_probe[nprobes] = 1043;
            nprobes += 1;

        }
        if (curves.contains(ROPHE) || curves.contains(ROAHE)) {
            if (curves.contains(ROPHE)) {
                arrPHE = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROPHE));
                bolPHE = true;
                length = arrPHE.length;
            }
            if (curves.contains(ROAHE)) {
                arrAHE = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROAHE));
                bolAHE = true;
                length = arrAHE.length;
            }
            num_probe[nprobes] = 1044;
            nprobes += 1;
        }*/
        double[] md2 = null;
        double[] tvd2 = null;
        double[] x2 = null;
        double[] zeni2 = null;
        double[] ro_by_phases = new double[nprobes * length];
        double[] ro_by_ampl = new double[nprobes * length];
        md2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(md, idProject).getCurveData());
        tvd2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(tvd, idProject).getCurveData());
        x2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(x, idProject).getCurveData());
        zeni2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(zeni, idProject).getCurveData());
        int npoints = md2.length;
        for (int i = 0, j = 0; i < length; i++) {
            if (bolPL || bolAL) {
                if (bolPL) {
                    ro_by_phases[i * nprobes + j] = arrPL[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolAL) {
                    ro_by_ampl[i * nprobes + j] = arrAL[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
                j++;
            }
            if (bolPLD || bolALD) {
                if (bolPLD) {
                    ro_by_phases[i * nprobes + j] = arrPLD[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolALD) {
                    ro_by_ampl[i * nprobes + j] = arrALD[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
                j++;
            }
            if (bolPLE || bolALE) {
                if (bolPLE) {
                    ro_by_phases[i * nprobes + j] = arrPLE[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolALE) {
                    ro_by_ampl[i * nprobes + j] = arrALE[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
                j++;
            }
            if (bolPH || bolAH) {
                if (bolPH) {
                    ro_by_phases[i * nprobes + j] = arrPH[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolAH) {
                    ro_by_ampl[i * nprobes + j] = arrAH[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
                j++;
            }
            if (bolPHD || bolAHD) {
                if (bolPHD) {
                    ro_by_phases[i * nprobes + j] = arrPHD[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolAHD) {
                    ro_by_ampl[i * nprobes + j] = arrAHD[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
                j++;
            }
            if (bolPHE || bolAHE) {
                if (bolPHE) {
                    ro_by_phases[i * nprobes + j] = arrPHE[i];
                } else {
                    ro_by_phases[i * nprobes + j] = 0;
                }
                if (bolAHE) {
                    ro_by_ampl[i * nprobes + j] = arrAHE[i];
                } else {
                    ro_by_ampl[i * nprobes + j] = 0;
                }
            }
            j = 0;
        }
        int range = inputParamAreasDTO.range;
        double[] tvd_startArr = new double[range];
        double[] alphaArr = new double[range];
        double[] ro_upArr = new double[range];
        double[] kanisotropy_upArr = new double[range];
        double[] ro_downArr = new double[range];
        double[] kanisotropy_downArr = new double[range];
        double[] param1 = new double[range];
        double[] param2 = new double[range];

        int tvd_startArrlength = 1;
        int alphaArrlength = 1;
        int ro_upArrlength = 1;
        int kanisotropy_upArrlength = 1;
        int ro_downArrlength = 1;
        int kanisotropy_downArrlength = 1;
        int param1length = 0;
        int param2length = 0;

        tvd_startArr[0] = modelEntity.getTvdStart();
        alphaArr[0] = modelEntity.getAlpha();
        ro_upArr[0] = modelEntity.getRoUp();
        kanisotropy_upArr[0] = modelEntity.getKanisotropyUp();
        ro_downArr[0] = modelEntity.getRoDown();
        kanisotropy_downArr[0] = modelEntity.getKanisotropyDown();

        if (Objects.equals(inputParamAreasDTO.param1, "tvd_start") ||
                Objects.equals(inputParamAreasDTO.param2, "tvd_start")) {
            for (int i = 0; i < range; i++) {
                tvd_startArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            tvd_startArrlength = range;
            if (param1length == 0) {
                param1 = tvd_startArr;
                param1length = range;
            }
            else {
                param2 = tvd_startArr;
            }
        }
        if (Objects.equals(inputParamAreasDTO.param1, "alpha") ||
                Objects.equals(inputParamAreasDTO.param2, "alpha")) {
            for (int i = 0; i < range; i++) {
                alphaArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            alphaArrlength = range;
            if (param1length == 0) {
                param1 = alphaArr;
                param1length = range;
            }
            else {
                param2 = alphaArr;
            }
        }
        if (Objects.equals(inputParamAreasDTO.param1, "ro_up") ||
                Objects.equals(inputParamAreasDTO.param2, "ro_up")) {
            for (int i = 0; i < range; i++) {
                ro_upArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            ro_upArrlength = range;
            if (param1length == 0) {
                param1 = ro_upArr;
                param1length = range;
            }
            else {
                param2 = ro_upArr;
            }
        }
        if (Objects.equals(inputParamAreasDTO.param1, "kanisotropy_up") ||
                Objects.equals(inputParamAreasDTO.param2, "kanisotropy_up")) {
            for (int i = 0; i < range; i++) {
                kanisotropy_upArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            kanisotropy_upArrlength = range;
            if (param1length == 0) {
                param1 = kanisotropy_upArr;
                param1length = range;
            }
            else {
                param2 = kanisotropy_upArr;
            }
        }
        if (Objects.equals(inputParamAreasDTO.param1, "ro_down") ||
                Objects.equals(inputParamAreasDTO.param2, "ro_down")) {
            for (int i = 0; i < range; i++) {
                ro_downArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            ro_downArrlength = range;
            if (param1length == 0) {
                param1 = ro_downArr;
                param1length = range;
            }
            else {
                param2 = ro_downArr;
            }
        }
        if (Objects.equals(inputParamAreasDTO.param1, "kanisotropy_down") ||
                Objects.equals(inputParamAreasDTO.param2, "kanisotropy_down")) {
            for (int i = 0; i < range; i++) {
                kanisotropy_downArr[i] = Math.pow(10, -1 + i * ((double) 4.0 / range));
            }
            kanisotropy_downArrlength = range;
            if (param1length == 0) {
                param1 = kanisotropy_downArr;
                param1length = range;
            }
            else {
                param2 = kanisotropy_downArr;
            }
        }

        InputAreasEquivalence inputAreasEquivalence = new InputAreasEquivalence(nprobes, num_probe, npoints,
        tvd2, x2, zeni2, tvd_startArrlength, tvd_startArr,
        alphaArrlength, alphaArr, ro_downArrlength,
        ro_upArr, kanisotropy_upArrlength,
        kanisotropy_upArr, ro_downArrlength,
        ro_downArr, kanisotropy_downArrlength,
        kanisotropy_downArr, ro_by_phases, ro_by_ampl);

        AreasEquivalence areasEquivalence = nativeLibrary.createAreasEquivalence(inputAreasEquivalence);

        ColorMapBuilder colorMapBuilder = new ColorMapBuilder();

        /*double[][] intensityValues = new double[param1length][param1length];
        for (int i = 0; i < param1length; i++) {
            for (int j = 0; j < param1length; j++) {
                intensityValues[i][j] = areasEquivalence.getTargetFunction()[i * param1length + j];
            }
        }

        colorMapBuilder.saveColorMapToFile(colorMapBuilder.generateLogarithmicColorMap(
                param1, param2, intensityValues, findMin(areasEquivalence.getTargetFunction()),
                findMax(areasEquivalence.getTargetFunction())), "ColorMap");*/
        System.out.println(convertToFloatList(ro_upArr));
        System.out.println(convertToFloatList(ro_downArr));
        System.out.println(convertToFloatList(areasEquivalence.getTargetFunction()));
        pythonService.sendIntensityDataAndReceiveImage
                (convertToFloatList(areasEquivalence.getTargetFunction()), range);
        return pythonService.sendIntensityDataAndReceiveImage
                (convertToFloatList(areasEquivalence.getTargetFunction()), range);
    }
}
