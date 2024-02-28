package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.repositories.ModelRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.file.LasFileService;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.nsu.fit.geodrilling.model.Constant.NAN;


@Service
@AllArgsConstructor
public class ModelService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final LasFileService lasFileService;
    private final NativeLibrary nativeLibrary;
    private final ModelRepository modelRepository;
    private double[] ListDoubleInDoubleArray(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }
    public ModelDTO createModel(Long idProject, String name, String email) throws Exception {
        Boolean bol = true;
        for (ProjectEntity projectEntity : userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getProjects()) {
            if (Objects.equals(projectEntity.getId(), idProject)) {
                bol = false;
            }
        }

        if (bol) {
            throw new Exception("Пользователя нет в проекте");
        }

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
        ProjectEntity projectEntity = projectRepository.findById(idProject).
                orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
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
                        getCurveDataByName(projectEntity, ROPL));
                bolPL = true;
                length = arrPL.length;
            }
            if (curves.contains(ROAL)) {
                arrAL = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROAL));
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
                        getCurveDataByName(projectEntity, ROPH));
                bolPH = true;
                length = arrPH.length;
            }
            if (curves.contains(ROAH)) {
                arrAH = ListDoubleInDoubleArray(lasFileService.
                        getCurveDataByName(projectEntity, ROAH));
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
        md2 = ListDoubleInDoubleArray(lasFileService.
                getCurveDataByName(projectRepository.findById(idProject).
                        orElseThrow(() -> new EntityNotFoundException("Проект не найден")), md));
        tvd2 = ListDoubleInDoubleArray(lasFileService.
                getCurveDataByName(projectRepository.findById(idProject).
                        orElseThrow(() -> new EntityNotFoundException("Проект не найден")), tvd));
        x2 = ListDoubleInDoubleArray(lasFileService.
                getCurveDataByName(projectRepository.findById(idProject).
                        orElseThrow(() -> new EntityNotFoundException("Проект не найден")), x));
        zeni2 = ListDoubleInDoubleArray(lasFileService.
                getCurveDataByName(projectRepository.findById(idProject).
                        orElseThrow(() -> new EntityNotFoundException("Проект не найден")), zeni));
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
        OutputModel outputModel = nativeLibrary.startModel(new InputBuildModel(nprobes, num_probe, npoints, md2, tvd2, x2, zeni2,
                ro_by_phases, ro_by_ampl, tvd_start, min_tvd_start, max_tvd_start, alpha,
                min_alpha, max_alpha, ro_up, kanisotropy_up, ro_down, kanisotropy_down));
        System.out.println(outputModel.getMisfit());
        System.out.println(outputModel.getTvdStart());
        System.out.println(outputModel.getRoUp());
        System.out.println(outputModel.getKanisotropyUp());
        System.out.println(outputModel.getRoDown());
        System.out.println(outputModel.getKanisotropyDown());
        alpha = 0;
        outputModel = nativeLibrary.solverModel(new InputBuildModel(nprobes, num_probe, npoints, md2, tvd2, x2, zeni2,
                ro_by_phases, ro_by_ampl, outputModel.getTvdStart(), min_tvd_start, max_tvd_start, alpha,
                min_alpha, max_alpha, outputModel.getRoUp(), outputModel.getKanisotropyUp(),
                outputModel.getRoDown(), outputModel.getKanisotropyDown()));
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setName(name);
        modelEntity.setMinKanisotropyDown(outputModel.getMinKanisotropyDown());
        modelEntity.setMaxKanisotropyDown(outputModel.getMaxKanisotropyDown());
        modelEntity.setSyntRoByPhases(outputModel.getSyntRoByPhases());
        modelEntity.setSyntRoByAmpl(outputModel.getSyntRoByAmpl());
        modelEntity.setMisfit(outputModel.getMisfit());
        modelEntity.setKanisotropyDown(outputModel.getKanisotropyDown());
        modelEntity.setMinRoDown(outputModel.getMinRoDown());
        modelEntity.setMaxRoDown(outputModel.getMaxRoDown());
        modelEntity.setRoDown(outputModel.getRoDown());
        modelEntity.setKanisotropyUp(outputModel.getKanisotropyUp());
        modelEntity.setMinKanisotropyUp(outputModel.getMinKanisotropyUp());
        modelEntity.setMaxKanisotropyUp(outputModel.getMaxKanisotropyUp());
        modelEntity.setRoUp(outputModel.getRoUp());
        modelEntity.setMinRoUp(outputModel.getMinRoUp());
        modelEntity.setMaxRoUp(outputModel.getMaxRoUp());
        modelEntity.setAlpha(outputModel.getAlpha());
        modelEntity.setTvdStart(outputModel.getTvdStart());
        modelEntity.setStatus(outputModel.getStatus());
        modelEntity.setProjectEntity(projectEntity);
        modelRepository.save(modelEntity);
        return new ModelDTO(modelEntity.getId(), modelEntity.getName(), outputModel);

    }
}