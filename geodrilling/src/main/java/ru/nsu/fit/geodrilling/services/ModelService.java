package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.geodrilling.dto.CurveDto;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.InputModelSignal;
import ru.nsu.fit.geodrilling.dto.ModelCreateRequest;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.dto.SaveModelResponse;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.model.ModelSignal;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ModelRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.UserRepository;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.nsu.fit.geodrilling.model.Constant.NAN;


@Service
@RequiredArgsConstructor
public class ModelService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CurvesService lasFileService;
    private final CurveRepository curveRepository;
    private final NativeLibrary nativeLibrary;
    private final ModelRepository modelRepository;

//  @Value("${projects.folder-path}")
//  private String projectsFolderPath;

    private double[] ListDoubleInDoubleArray(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public InputBuildModel createInputBuildModel(Long idProject, boolean log, Double start, Double end) {
        List<String> curves = new ArrayList<>(
                lasFileService.getCurvesNames(idProject).getCurvesNames());
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
        ProjectEntity projectEntity = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
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
//    String md = projectEntity.getSootEntity().getMd();
        /*System.out.println( lasFileService.getRange(projectEntity, "DEPT", start, end, false).size());*/

        String tvd = projectEntity.getSootEntity().getTvd();
        String x = projectEntity.getSootEntity().getX();
        String zeni = projectEntity.getSootEntity().getZeni();
        int length = 0;
        if (curves.contains(ROPL) || curves.contains(ROAL)) {
            if (curves.contains(ROPL)) {
                arrPL = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, ROPL, start, end, false));
                bolPL = true;
                length = arrPL.length;
            }
            if (curves.contains(ROAL)) {
                /*System.out.println(ROAL);*/
                arrAL = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, ROAL, start, end, false));
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
                /*System.out.println(ROPH);*/
                arrPH = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, ROPH, start, end, false));
                bolPH = true;
                length = arrPH.length;
            }
            if (curves.contains(ROAH)) {
                arrAH = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, ROAH, start, end, false));
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
        //md2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(md, idProject).getCurveData());
//    tvd2 = ListDoubleInDoubleArray(
//        lasFileService.getCurveDataByName(tvd, idProject).getCurveData());
//    x2 = ListDoubleInDoubleArray(lasFileService.getCurveDataByName(x, idProject).getCurveData());
//    zeni2 = ListDoubleInDoubleArray(
//        lasFileService.getCurveDataByName(zeni, idProject).getCurveData());
        /*System.out.println(tvd);*/
        tvd2 = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, tvd, start, end, false));
        x2 = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, x, start, end, false));
        zeni2 = ListDoubleInDoubleArray(lasFileService.getRange(projectEntity, zeni, start, end, false));
        md2 = x2;
        int npoints = tvd2.length;
        if (log) {
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
        }
        return new InputBuildModel(nprobes, num_probe, npoints, md2, tvd2, x2, zeni2,
                ro_by_phases, ro_by_ampl, tvd_start, min_tvd_start, max_tvd_start, alpha,
                min_alpha, max_alpha, ro_up, NAN, NAN, kanisotropy_up, NAN, NAN, ro_down, NAN, NAN,
                kanisotropy_down, NAN, NAN);
    }

    public ModelDTO createModel(String email, ModelCreateRequest modelCreateRequest, Long idProject) {
        ProjectEntity projectEntity = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        if (!Objects.equals(projectEntity.getUser().getEmail(), email)) {
            throw new EntityNotFoundException("Проект не найден");
        }
        ModelDTO modelDTO = modelCreateRequest.getModelDTO();
        InputBuildModel inputBuildModel = createInputBuildModel(idProject, true, modelDTO.getStart(), modelDTO.getEnd());

        inputBuildModel.setTvd_start(modelDTO.getTvdStart());
        inputBuildModel.setRo_up(modelDTO.getRoUp());
        inputBuildModel.setKanisotropy_up(modelDTO.getKanisotropyUp());
        inputBuildModel.setRo_down(modelDTO.getRoDown());
        inputBuildModel.setKanisotropy_down(modelDTO.getKanisotropyDown());
        inputBuildModel.setAlpha(modelDTO.getAlpha());

        /*System.out.println(modelCreateRequest.getRangeParameters().getMax_ro_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_ro_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_alpha());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_alpha());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_tvd_start());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_tvd_start());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_kanisotropy_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_kanisotropy_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_ro_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_ro_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_kanisotropy_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_kanisotropy_down());*/

        setDefaultValues(modelCreateRequest.getRangeParameters());

/*    System.out.println(inputBuildModel.getKanisotropy_down());
    System.out.println(inputBuildModel.getKanisotropy_up());
    System.out.println(inputBuildModel.getRo_down());
    System.out.println(inputBuildModel.getRo_up());
    System.out.println(inputBuildModel.getAlpha());
    System.out.println(inputBuildModel.getTvd_start());*/

       /* System.out.println("====");
        System.out.println(modelCreateRequest.getRangeParameters().getMax_ro_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_ro_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_alpha());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_alpha());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_tvd_start());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_tvd_start());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_kanisotropy_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_kanisotropy_up());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_ro_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_ro_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMax_kanisotropy_down());
        System.out.println(modelCreateRequest.getRangeParameters().getMin_kanisotropy_down());
        System.out.println("====");*/

        inputBuildModel.setMax_alpha(modelCreateRequest.getRangeParameters().getMax_alpha());
        inputBuildModel.setMin_alpha(modelCreateRequest.getRangeParameters().getMin_alpha());
        inputBuildModel.setMax_tvd_start(modelCreateRequest.getRangeParameters().getMax_tvd_start());
        inputBuildModel.setMin_tvd_start(modelCreateRequest.getRangeParameters().getMin_tvd_start());
        inputBuildModel.setMax_ro_up(modelCreateRequest.getRangeParameters().getMax_ro_up());
        inputBuildModel.setMin_ro_up(modelCreateRequest.getRangeParameters().getMin_ro_up());
        inputBuildModel.setMax_ro_down(modelCreateRequest.getRangeParameters().getMax_ro_down());
        inputBuildModel.setMin_ro_down(modelCreateRequest.getRangeParameters().getMin_ro_down());
        inputBuildModel.setMax_kanisotropy_up(
                modelCreateRequest.getRangeParameters().getMax_kanisotropy_up());
        inputBuildModel.setMin_kanisotropy_up(
                modelCreateRequest.getRangeParameters().getMin_kanisotropy_up());
        inputBuildModel.setMax_kanisotropy_down(
                modelCreateRequest.getRangeParameters().getMax_kanisotropy_down());
        inputBuildModel.setMin_kanisotropy_down(
                modelCreateRequest.getRangeParameters().getMin_kanisotropy_down());

        /*System.out.println("====");
        System.out.println(inputBuildModel.getMax_alpha());
        System.out.println(inputBuildModel.getMin_alpha());
        System.out.println(inputBuildModel.getMax_tvd_start());
        System.out.println(inputBuildModel.getMin_tvd_start());
        System.out.println(inputBuildModel.getMax_ro_up());
        System.out.println(inputBuildModel.getMin_ro_up());
        System.out.println(inputBuildModel.getMax_ro_down());
        System.out.println(inputBuildModel.getMin_ro_down());
        System.out.println(inputBuildModel.getMax_kanisotropy_up());
        System.out.println(inputBuildModel.getMin_kanisotropy_up());
        System.out.println(inputBuildModel.getMax_kanisotropy_down());
        System.out.println(inputBuildModel.getMin_kanisotropy_down());*/
        OutputModel outputModel = nativeLibrary.solverModel(inputBuildModel);/*
        System.out.println("====");
        System.out.println(outputModel.getMisfit());*/


        /*OutputModel outputModel = nativeLibrary.startModel(new InputBuildModel(nprobes, num_probe, npoints, md2, tvd2, x2, zeni2,
                ro_by_phases, ro_by_ampl, tvd_start, min_tvd_start, max_tvd_start, alpha,
                min_alpha, max_alpha, ro_up, kanisotropy_up, ro_down, kanisotropy_down));
        System.out.println(outputModel.getMisfit());
        System.out.println(outputModel.getTvdStart());
        System.out.println(outputModel.getRoUp());
        System.out.println(outputModel.getKanisotropyUp());
        System.out.println(outputModel.getRoDown());
        System.out.println(outputModel.getKanisotropyDown());
        double alpha = 0;*/
//        outputModel = nativeLibrary.solverModel(new InputBuildModel(nprobes, num_probe, npoints, md2, tvd2, x2, zeni2,
//                ro_by_phases, ro_by_ampl, outputModel.getTvdStart(), min_tvd_start, max_tvd_start, alpha,
//                min_alpha, max_alpha, outputModel.getRoUp(), outputModel.getKanisotropyUp(),
//                outputModel.getRoDown(), outputModel.getKanisotropyDown()));
       /* ModelEntity modelEntity = new ModelEntity();
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
        modelEntity.setProjectEntity(projectEntity);*/
        // modelRepository.save(modelEntity);
        return new ModelDTO(0L, "", modelDTO.getStart(), modelDTO.getEnd(), outputModel.getKanisotropyDown(), outputModel.getRoDown(),
                outputModel.getKanisotropyUp(), outputModel.getRoUp(),
                outputModel.getAlpha(), outputModel.getTvdStart());

    }

    public ModelDTO createStartModel(String email, Long idProject, Double start, Double end) {
        ProjectEntity projectEntity = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        if (!Objects.equals(projectEntity.getUser().getEmail(), email)) {
            throw new EntityNotFoundException("Проект не найден");
        }

        InputBuildModel inputBuildModel = createInputBuildModel(idProject, true, start, end);/*
        System.out.println(inputBuildModel.getNpoints());
        System.out.println(Arrays.toString(inputBuildModel.getX()));
        System.out.println(inputBuildModel.getX().length);
        System.out.println(Arrays.toString(inputBuildModel.getZeni()));
        System.out.println(inputBuildModel.getZeni().length);
        System.out.println(Arrays.toString(inputBuildModel.getTvd()));
        System.out.println(inputBuildModel.getTvd().length);
        System.out.println(Arrays.toString(inputBuildModel.ro_by_ampl));
        System.out.println(Arrays.toString(inputBuildModel.ro_by_phases));*/

//
//        System.out.println();
//        System.out.println();
//        System.out.println();
        OutputModel outputModel = nativeLibrary.startModel(inputBuildModel);/*
        System.out.println(outputModel.getMisfit());
        System.out.println(outputModel.getTvdStart());
        System.out.println(outputModel.getRoUp());
        System.out.println(outputModel.getKanisotropyUp());
        System.out.println(outputModel.getRoDown());
        System.out.println(outputModel.getKanisotropyDown());*/
        return new ModelDTO(0L, "start", start, end, outputModel.getKanisotropyDown(),
                outputModel.getRoDown(), outputModel.getKanisotropyUp(), outputModel.getRoUp(),
                outputModel.getAlpha(), outputModel.getTvdStart());
    }

    @Transactional
    public SaveModelResponse saveModel(String email, ModelDTO modelDTO, Long idProject) {
        ProjectEntity projectEntity = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        if (!Objects.equals(projectEntity.getUser().getEmail(), email)) {
            throw new EntityNotFoundException("Проект не найден");
        }
        boolean log = true;
        List<ModelEntity> modelEntityList = modelRepository.findByProjectEntityOrderByStartXAsc(projectEntity);
        if (modelEntityList.size() == 0) {
            log = false;
        }/*
        System.out.println(modelEntityList.size());*/
        List<ModelDTO> modelDTOList = new ArrayList<>();
        if (log) {
            for (ModelEntity modelEntity : modelEntityList) {
                if (modelEntity.getStartX() >= modelDTO.getStart()
                        && modelEntity.getEndX() <= modelDTO.getEnd()) {
                    //del
                    modelRepository.delete(modelEntity);
                    continue;
                }

                if (modelEntity.getStartX() >= modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()
                        && modelEntity.getStartX() < modelDTO.getEnd()) {
                    // c start
                    modelEntity.setStartX(modelDTO.getEnd());
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (/*modelEntity.getStartX() > modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()
                        &&*/ modelEntity.getStartX() >= modelDTO.getEnd()) {
                    // save
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() <= modelDTO.getEnd()
                        && modelEntity.getEndX() > modelDTO.getStart()) {
                    // c end
                    modelEntity.setEndX(modelDTO.getStart());
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (/*modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() < modelDTO.getEnd()
                        &&*/ modelEntity.getEndX() <= modelDTO.getStart()) {
                    //save
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()) {
                    // razrez
                    ModelEntity modelEntity2 = new ModelEntity();

                    modelEntity2.setStartX(modelDTO.getEnd());
                    modelEntity2.setEndX(modelEntity.getEndX());
                    modelEntity2.setAlpha(modelEntity.getAlpha());
                    modelEntity2.setTvdStart(modelEntity.getTvdStart());
                    modelEntity2.setRoDown(modelEntity.getRoDown());
                    modelEntity2.setRoUp(modelEntity.getRoUp());
                    modelEntity2.setKanisotropyDown(modelEntity.getKanisotropyDown());
                    modelEntity2.setKanisotropyUp(modelEntity.getKanisotropyUp());

                    modelEntity.setEndX(modelDTO.getStart());

                    modelEntity2.setProjectEntity(projectEntity);
                    projectEntity.getModelEntityList().add(modelEntity2);
                    modelRepository.save(modelEntity2);

                    modelDTOList.add(mapModelDto(modelEntity));
                    modelDTOList.add(mapModelDto(modelEntity2));
                }

            }
        }
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setStartX(modelDTO.getStart());
        modelEntity.setEndX(modelDTO.getEnd());
        modelEntity.setKanisotropyDown(modelDTO.getKanisotropyDown());
        modelEntity.setRoDown(modelDTO.getRoDown());
        modelEntity.setKanisotropyUp(modelDTO.getKanisotropyUp());
        modelEntity.setRoUp(modelDTO.getRoUp());
        modelEntity.setAlpha(modelDTO.getAlpha());
        modelEntity.setTvdStart(modelDTO.getTvdStart());
        if (modelDTO.getName() != null) {
            modelEntity.setName(modelDTO.getName());
        } else {
            modelEntity.setName(modelDTO.getStart().toString() + "-" + modelDTO.getEnd().toString());
        }
        modelEntity.setProjectEntity(projectEntity);
        projectEntity.getModelEntityList().add(modelEntity);
        modelRepository.save(modelEntity);

        modelDTOList.add(mapModelDto(modelEntity));

        Collections.sort(modelDTOList, new Comparator<ModelDTO>() {
            @Override
            public int compare(ModelDTO o1, ModelDTO o2) {
                return Double.compare(o1.getStart(), o2.getStart());
            }
        });


        InputBuildModel inputBuildModel = createInputBuildModel(idProject, false, modelDTO.getStart(), modelDTO.getEnd());

        ModelSignal modelSignal = nativeLibrary.simulateModelSignal(
                new InputModelSignal(inputBuildModel.getNprobes(), inputBuildModel.getNum_probe(),
                        inputBuildModel.getNpoints(), inputBuildModel.getTvd(),
                        inputBuildModel.getX(), inputBuildModel.getZeni(), modelDTO.getTvdStart(),
                        modelDTO.getAlpha(), modelDTO.getRoUp(), modelDTO.getKanisotropyUp(),
                        modelDTO.getRoDown(), modelDTO.getKanisotropyDown()));/*
        System.out.print("getNpoints=");
        System.out.println(inputBuildModel.getNpoints());*/
        int n = inputBuildModel.getNprobes();/*
        System.out.print("n=");
        System.out.println(n);*/
        int elementsPerPart = modelSignal.getSyntRoByAmpl().length / n;/*
        System.out.print("elementsPerPart=");
        System.out.println(elementsPerPart);*/
        List<CurveDto> curveDtoList = new ArrayList<>();
        double[][] partAmpl = new double[n][elementsPerPart];
        double[][] partPhases = new double[n][elementsPerPart];
        for (int i = 0; i < elementsPerPart; i++) {
            for (int j = 0; j < n; j++) {
                partAmpl[j][i] = modelSignal.getSyntRoByAmpl()[i * n + j];
                partPhases[j][i] = modelSignal.getSyntRoByPhases()[i * n + j];
            }
        }
        int[] numProbe = inputBuildModel.getNum_probe();
        SootEntity sootEntity = projectEntity.getSootEntity();
        List<String> nameCurveList = getSootName(sootEntity);/*
        System.out.println(log);*/
        for (int j = 0; j < n; j++) {
            String name = getName(nameCurveList, numProbe[j], true);
            List<Double> list = new ArrayList<>();
            for (double value : partAmpl[j]) {
                list.add(value);
            }

            List<String> curves = new ArrayList<>(
                    lasFileService.getCurvesNames(idProject).getCurvesNames());

            if (curves.contains(name)) { // временая проверкка
                if (log) {
                    lasFileService.changeRange(projectEntity, name, modelDTO.getStart(), list, true);
                } else {
                    lasFileService.saveSyntheticCurve(projectEntity, name, list);
                }/*
                System.out.println("npoints = " + inputBuildModel.npoints);
                System.out.println(list.size());*/
                curveDtoList.add(new CurveDto("/synthetic/" + name,
                        lasFileService.getCurveDataByName(name, idProject, true).getCurveData() ));
            }
            name = getName(nameCurveList, numProbe[j], false);
            list = new ArrayList<>();
            for (double value : partAmpl[j]) {
                list.add(value);
            }
            if (curves.contains(name)) { // временая проверкка
                if (log) {
                    lasFileService.changeRange(projectEntity, name, modelDTO.getStart(), list, true);
                } else {
                    lasFileService.saveSyntheticCurve(projectEntity, name, list);
                }

                curveDtoList.add(new CurveDto(
                         "/synthetic/" + name,
                        lasFileService.getCurveDataByName(name, idProject, true).getCurveData() ));/*
                System.out.println("npoints = " + inputBuildModel.npoints);
                System.out.println(list.size());*/
            }
        }


        projectRepository.save(projectEntity);
        return new SaveModelResponse(modelDTOList, curveDtoList);
    }
    @Transactional
    public SaveModelResponse saveModel(ModelDTO modelDTO, ProjectEntity projectEntity) {
        boolean log = true;
        List<ModelEntity> modelEntityList = modelRepository.findByProjectEntityOrderByStartXAsc(projectEntity);
        if (modelEntityList.size() == 0) {
            log = false;
        }
        System.out.println(modelEntityList.size());
        List<ModelDTO> modelDTOList = new ArrayList<>();
        if (log) {
            for (ModelEntity modelEntity : modelEntityList) {
                if (modelEntity.getStartX() >= modelDTO.getStart()
                        && modelEntity.getEndX() <= modelDTO.getEnd()) {
                    //del
                    modelRepository.delete(modelEntity);
                    continue;
                }

                if (modelEntity.getStartX() >= modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()
                        && modelEntity.getStartX() < modelDTO.getEnd()) {
                    // c start
                    modelEntity.setStartX(modelDTO.getEnd());
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (/*modelEntity.getStartX() > modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()
                        &&*/ modelEntity.getStartX() >= modelDTO.getEnd()) {
                    // save
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() <= modelDTO.getEnd()
                        && modelEntity.getEndX() > modelDTO.getStart()) {
                    // c end
                    modelEntity.setEndX(modelDTO.getStart());
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (/*modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() < modelDTO.getEnd()
                        &&*/ modelEntity.getEndX() <= modelDTO.getStart()) {
                    //save
                    modelDTOList.add(mapModelDto(modelEntity));
                    continue;
                }

                if (modelEntity.getStartX() < modelDTO.getStart()
                        && modelEntity.getEndX() > modelDTO.getEnd()) {
                    // razrez
                    ModelEntity modelEntity2 = new ModelEntity();

                    modelEntity2.setStartX(modelDTO.getEnd());
                    modelEntity2.setEndX(modelEntity.getEndX());
                    modelEntity2.setAlpha(modelEntity.getAlpha());
                    modelEntity2.setTvdStart(modelEntity.getTvdStart());
                    modelEntity2.setRoDown(modelEntity.getRoDown());
                    modelEntity2.setRoUp(modelEntity.getRoUp());
                    modelEntity2.setKanisotropyDown(modelEntity.getKanisotropyDown());
                    modelEntity2.setKanisotropyUp(modelEntity.getKanisotropyUp());

                    modelEntity.setEndX(modelDTO.getStart());

                    modelEntity2.setProjectEntity(projectEntity);
                    projectEntity.getModelEntityList().add(modelEntity2);
                    modelRepository.save(modelEntity2);

                    modelDTOList.add(mapModelDto(modelEntity));
                    modelDTOList.add(mapModelDto(modelEntity2));
                }

            }
        }
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setStartX(modelDTO.getStart());
        modelEntity.setEndX(modelDTO.getEnd());
        modelEntity.setKanisotropyDown(modelDTO.getKanisotropyDown());
        modelEntity.setRoDown(modelDTO.getRoDown());
        modelEntity.setKanisotropyUp(modelDTO.getKanisotropyUp());
        modelEntity.setRoUp(modelDTO.getRoUp());
        modelEntity.setAlpha(modelDTO.getAlpha());
        modelEntity.setTvdStart(modelDTO.getTvdStart());
        if (modelDTO.getName() != null) {
            modelEntity.setName(modelDTO.getName());
        } else {
            modelEntity.setName(modelDTO.getStart().toString() + "-" + modelDTO.getEnd().toString());
        }
        modelEntity.setProjectEntity(projectEntity);
        projectEntity.getModelEntityList().add(modelEntity);
        modelRepository.save(modelEntity);

        modelDTOList.add(mapModelDto(modelEntity));

        Collections.sort(modelDTOList, new Comparator<ModelDTO>() {
            @Override
            public int compare(ModelDTO o1, ModelDTO o2) {
                return Double.compare(o1.getStart(), o2.getStart());
            }
        });


        InputBuildModel inputBuildModel = createInputBuildModel(projectEntity.getId(), false, modelDTO.getStart(), modelDTO.getEnd());

        ModelSignal modelSignal = nativeLibrary.simulateModelSignal(
                new InputModelSignal(inputBuildModel.getNprobes(), inputBuildModel.getNum_probe(),
                        inputBuildModel.getNpoints(), inputBuildModel.getTvd(),
                        inputBuildModel.getX(), inputBuildModel.getZeni(), modelDTO.getTvdStart(),
                        modelDTO.getAlpha(), modelDTO.getRoUp(), modelDTO.getKanisotropyUp(),
                        modelDTO.getRoDown(), modelDTO.getKanisotropyDown()));/*
        System.out.print("getNpoints=");
        System.out.println(inputBuildModel.getNpoints());*/
        int n = inputBuildModel.getNprobes();/*
        System.out.print("n=");
        System.out.println(n);*/
        int elementsPerPart = modelSignal.getSyntRoByAmpl().length / n;/*
        System.out.print("elementsPerPart=");
        System.out.println(elementsPerPart);*/
        List<CurveDto> curveDtoList = new ArrayList<>();
        double[][] partAmpl = new double[n][elementsPerPart];
        double[][] partPhases = new double[n][elementsPerPart];
        for (int i = 0; i < elementsPerPart; i++) {
            for (int j = 0; j < n; j++) {
                partAmpl[j][i] = modelSignal.getSyntRoByAmpl()[i * n + j];
                partPhases[j][i] = modelSignal.getSyntRoByPhases()[i * n + j];
            }
        }
        int[] numProbe = inputBuildModel.getNum_probe();
        SootEntity sootEntity = projectEntity.getSootEntity();
        List<String> nameCurveList = getSootName(sootEntity);
        /*System.out.println(log);*/
        for (int j = 0; j < n; j++) {
            String name = getName(nameCurveList, numProbe[j], true);
            List<Double> list = new ArrayList<>();
            for (double value : partAmpl[j]) {
                list.add(value);
            }

            List<String> curves = new ArrayList<>(
                    lasFileService.getCurvesNames(projectEntity.getId()).getCurvesNames());

            if (curves.contains(name)) { // временая проверкка
                if (log) {
                    lasFileService.changeRange(projectEntity, name, modelDTO.getStart(), list, true);
                } else {
                    lasFileService.saveSyntheticCurve(projectEntity, name, list);
                }

                curveDtoList.add(new CurveDto("synthetic/" + name,
                        lasFileService.getCurveDataByName(name, projectEntity.getId(), true).getCurveData() ));
            }
            name = getName(nameCurveList, numProbe[j], false);
            list = new ArrayList<>();
            for (double value : partAmpl[j]) {
                list.add(value);
            }
            if (curves.contains(name)) { // временая проверкка
                if (log) {
                    lasFileService.changeRange(projectEntity, name, modelDTO.getStart(), list, true);
                } else {
                    lasFileService.saveSyntheticCurve(projectEntity, name, list);
                }

                curveDtoList.add(new CurveDto(
                        "synthetic/" + name,
                        lasFileService.getCurveDataByName(name, projectEntity.getId(), true).getCurveData() ));
            }
        }


        projectRepository.save(projectEntity);
        return new SaveModelResponse(modelDTOList, curveDtoList);
    }

    private List<String> getSootName(SootEntity sootEntity) {
        List<String> stringList = new ArrayList<>();
        stringList.add(sootEntity.getROAL());
        stringList.add(sootEntity.getROALD());
        stringList.add(sootEntity.getROALE());
        stringList.add(sootEntity.getROAH());
        stringList.add(sootEntity.getROAHD());
        stringList.add(sootEntity.getROAHE());

        stringList.add(sootEntity.getROPL());
        stringList.add(sootEntity.getROPLD());
        stringList.add(sootEntity.getROPLE());
        stringList.add(sootEntity.getROPH());
        stringList.add(sootEntity.getROPHD());
        stringList.add(sootEntity.getROPHE());
        return stringList;
    }

    private void setDefaultValues(Object dto) {
        if (dto == null) {
            return;
        }

        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Дает доступ к приватным полям
            try {
                // Проверка на null и установка значения в зависимости от типа
                if (field.get(dto) == null) {
                    // Пример для строк и целых чисел. Добавьте свои типы по необходимости
                    if (field.getType().equals(Double.class)) {
                        field.set(dto, NAN);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private String getName(List<String> sootNameList, int num, boolean f) {
        if (f) {
            return sootNameList.get(num - 1039);
        }
        return sootNameList.get(num - 1039 + 6);
    }

    public List<ModelDTO> getModel(String email, Long idProject) {
        ProjectEntity projectEntity = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        if (!Objects.equals(projectEntity.getUser().getEmail(), email)) {
            throw new EntityNotFoundException("Проект не найден");
        }
        return mapModelDtoList(modelRepository.findByProjectEntityOrderByStartXAsc(projectEntity));
    }

    public ModelDTO mapModelDto(ModelEntity modelEntity) {
        return new ModelDTO(modelEntity.getId(), modelEntity.getName(), modelEntity.getStartX(),
                modelEntity.getEndX(), modelEntity.getKanisotropyDown(), modelEntity.getRoDown(),
                modelEntity.getKanisotropyUp(), modelEntity.getRoUp(), modelEntity.getAlpha(),
                modelEntity.getTvdStart());
    }

    public List<ModelDTO> mapModelDtoList(List<ModelEntity> modelEntityList) {
        List<ModelDTO> modelDTOList = new ArrayList<>();
        for (ModelEntity modelEntity : modelEntityList) {
            modelDTOList.add(mapModelDto(modelEntity));
        }
        return modelDTOList;
    }

/*    public SaveModelResponse updateModelSignal(ProjectEntity projectEntity){
        List<ModelEntity> modelEntityList = projectEntity.getModelEntityList();
        List<CurveDto> curveDtoList = new ArrayList<>();
        int n;
        SootEntity sootEntity = projectEntity.getSootEntity();
        List<String> nameCurveList = getSootName(sootEntity);
        for (ModelEntity modelEntity : modelEntityList){
            InputBuildModel inputBuildModel = createInputBuildModel(projectEntity.getId(), false, modelEntity.getStartX(), modelEntity.getEndX());

            ModelSignal modelSignal = nativeLibrary.simulateModelSignal(
                    new InputModelSignal(inputBuildModel.getNprobes(), inputBuildModel.getNum_probe(),
                            inputBuildModel.getNpoints(), inputBuildModel.getTvd(),
                            inputBuildModel.getX(), inputBuildModel.getZeni(), modelEntity.getTvdStart(),
                            modelEntity.getAlpha(), modelEntity.getRoUp(), modelEntity.getKanisotropyUp(),
                            modelEntity.getRoDown(), modelEntity.getKanisotropyDown()));


            n = inputBuildModel.getNprobes();
            int elementsPerPart = modelSignal.getSyntRoByAmpl().length / n;
            System.out.print("elementsPerPart=");
            System.out.println(elementsPerPart);
            double[][] partAmpl = new double[n][elementsPerPart];
            double[][] partPhases = new double[n][elementsPerPart];
            for (int i = 0; i < elementsPerPart; i++) {
                for (int j = 0; j < n; j++) {
                    partAmpl[j][i] = modelSignal.getSyntRoByAmpl()[i * n + j];
                    partPhases[j][i] = modelSignal.getSyntRoByPhases()[i * n + j];
                }
            }
            int[] numProbe = inputBuildModel.getNum_probe();

            for (int j = 0; j < n; j++) {
                String name = getName(nameCurveList, numProbe[j], true);
                List<Double> list = new ArrayList<>();
                for (double value : partAmpl[j]) {
                    list.add(value);
                }

                List<String> curves = new ArrayList<>(
                        lasFileService.getCurvesNames(projectEntity.getId()).getCurvesNames());

                if (curves.contains(name)) { // временая проверкка
                    lasFileService.changeRange(projectEntity, name, modelEntity.getStartX(), list, true);
                }
                name = getName(nameCurveList, numProbe[j], false);
                list = new ArrayList<>();
                for (double value : partAmpl[j]) {
                    list.add(value);
                }
                if (curves.contains(name)) { // временая проверкка
                    lasFileService.changeRange(projectEntity, name, modelEntity.getStartX(), list, true);

                }
            }
        }
        for (String nameCurve: nameCurveList) {
            curveDtoList.add(new CurveDto(
                    "synthetic/" + nameCurve,
                    lasFileService.getCurveDataByName(nameCurve, projectEntity.getId(), true).getCurveData() ));
        }

        List<ModelDTO> modelDTOList = mapModelDtoList(modelEntityList);
        return new SaveModelResponse(modelDTOList, curveDtoList);
    }*/

/*        public SaveModelResponse updateModelSignal(ProjectEntity projectEntity, List <Double> DeptOld){
        List<ModelEntity> modelEntityList = projectEntity.getModelEntityList();
        List<CurveDto> curveDtoList = new ArrayList<>();
        int n;
        SootEntity sootEntity = projectEntity.getSootEntity();
        List<String> nameCurveList = getSootName(sootEntity);
        for (ModelEntity modelEntity : modelEntityList){
            InputBuildModel inputBuildModel = createInputBuildModel(projectEntity.getId(), false, modelEntity.getStartX(), modelEntity.getEndX());

            ModelSignal modelSignal = nativeLibrary.simulateModelSignal(
                    new InputModelSignal(inputBuildModel.getNprobes(), inputBuildModel.getNum_probe(),
                            inputBuildModel.getNpoints(), inputBuildModel.getTvd(),
                            inputBuildModel.getX(), inputBuildModel.getZeni(), modelEntity.getTvdStart(),
                            modelEntity.getAlpha(), modelEntity.getRoUp(), modelEntity.getKanisotropyUp(),
                            modelEntity.getRoDown(), modelEntity.getKanisotropyDown()));


            n = inputBuildModel.getNprobes();
            int elementsPerPart = modelSignal.getSyntRoByAmpl().length / n;
            System.out.print("elementsPerPart=");
            System.out.println(elementsPerPart);
            double[][] partAmpl = new double[n][elementsPerPart];
            double[][] partPhases = new double[n][elementsPerPart];
            for (int i = 0; i < elementsPerPart; i++) {
                for (int j = 0; j < n; j++) {
                    partAmpl[j][i] = modelSignal.getSyntRoByAmpl()[i * n + j];
                    partPhases[j][i] = modelSignal.getSyntRoByPhases()[i * n + j];
                }
            }
            int[] numProbe = inputBuildModel.getNum_probe();

            for (int j = 0; j < n; j++) {
                String name = getName(nameCurveList, numProbe[j], true);
                List<Double> list = new ArrayList<>();
                for (double value : partAmpl[j]) {
                    list.add(value);
                }

                List<String> curves = new ArrayList<>(
                        lasFileService.getCurvesNames(projectEntity.getId()).getCurvesNames());

                if (curves.contains(name)) { // временая проверкка
                    lasFileService.changeRange(projectEntity, name, modelEntity.getStartX(), list, true);
                }
                name = getName(nameCurveList, numProbe[j], false);
                list = new ArrayList<>();
                for (double value : partAmpl[j]) {
                    list.add(value);
                }
                if (curves.contains(name)) { // временая проверкка
                    lasFileService.changeRange(projectEntity, name, modelEntity.getStartX(), list, true);

                }
            }
        }
        for (String nameCurve: nameCurveList) {
            curveDtoList.add(new CurveDto(
                    "synthetic/" + nameCurve,
                    lasFileService.getCurveDataByName(nameCurve, projectEntity.getId(), true).getCurveData() ));
        }

        List<ModelDTO> modelDTOList = mapModelDtoList(modelEntityList);
        return new SaveModelResponse(modelDTOList, curveDtoList);
    }*/

}
