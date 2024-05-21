package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.MaxMinDTO;
import ru.nsu.fit.geodrilling.dto.ModelDTO;
import ru.nsu.fit.geodrilling.dto.SootinDTO;
import ru.nsu.fit.geodrilling.dto.SootoutDTO;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class SootService {

  private ProjectRepository projectRepository;
  private SootRepository sootRepository;
  private CurvesService curvesService;
  private  final InterpolationService interpolationService;
  private final Gson gson;
  private final ModelService modelService;
  private  final CurveRepository curveRepository;

 /* public  void printCurves(CurveEntity zeni1, CurveEntity zeni2) {
    // Извлекаем и преобразуем данные первой кривой
    Double[] curve1 = extractCurve(zeni1);
    // Извлекаем и преобразуем данные второй кривой
    Double[] curve2 = extractCurve(zeni2);

    // Выводим элементы кривых построчно
    int maxLength = Math.max(curve1.length, curve2.length);
    for (int i = 0; i < maxLength; i++) {
      Double element1 = i < curve1.length ? curve1[i] : null;
      Double element2 = i < curve2.length ? curve2[i] : null;
      System.out.println((element1 != null ? element1 : "null") + " " + (element2 != null ? element2 : "null"));
    }
  }

  private  Double[] extractCurve(CurveEntity zeni) {
    TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};
    return CurveDataDownloadResponse.builder()
            .curveData(gson.fromJson(zeni
                    .getData(), floatListTypeToken))
            .build().getCurveData().toArray(Double[]::new);
  }*/
  public MaxMinDTO sootRename(Long idProject, SootinDTO sootinDTO) {
    ProjectEntity project = projectRepository.findById(idProject)
        .orElseThrow(
            () -> new EntityNotFoundException("Проект не найден по указанному id: " + idProject));
    MaxMinDTO maxMinDTO = null;
    SootEntity sootEntity = project.getSootEntity();
    if (!Objects.equals(sootinDTO.getRopl(), "--")) {
      sootEntity.setROPL(sootinDTO.getRopl());
      sootEntity.setROPLp(1);
    }
    if (!Objects.equals(sootinDTO.getRoal(), "--")) {
      sootEntity.setROAL(sootinDTO.getRoal());
      sootEntity.setROALp(1);
    }
    if (!Objects.equals(sootinDTO.getRopld(), "--")) {
      sootEntity.setROPLD(sootinDTO.getRopld());
      sootEntity.setROPLDp(1);
    }
    if (!Objects.equals(sootinDTO.getRoald(), "--")) {
      sootEntity.setROALD(sootinDTO.getRoald());
      sootEntity.setROALDp(1);
    }
    if (!Objects.equals(sootinDTO.getRople(), "--")) {
      sootEntity.setROPLE(sootinDTO.getRople());
      sootEntity.setROPLEp(1);
    }
    if (!Objects.equals(sootinDTO.getRoale(), "--")) {
      sootEntity.setROALE(sootinDTO.getRoale());
      sootEntity.setROALEp(1);
    }
    if (!Objects.equals(sootinDTO.getRoph(), "--")) {
      sootEntity.setROPH(sootinDTO.getRoph());
      sootEntity.setROPHp(1);
    }
    if (!Objects.equals(sootinDTO.getRoah(), "--")) {
      sootEntity.setROAH(sootinDTO.getRoah());
      sootEntity.setROAHp(1);
    }
    if (!Objects.equals(sootinDTO.getRophd(), "--")) {
      sootEntity.setROPHD(sootinDTO.getRophd());
      sootEntity.setROPHDp(1);
    }
    if (!Objects.equals(sootinDTO.getRoahd(), "--")) {
      sootEntity.setROAHD(sootinDTO.getRoahd());
      sootEntity.setROAHDp(1);
    }
    if (!Objects.equals(sootinDTO.getRophe(), "--")) {
      sootEntity.setROPHE(sootinDTO.getRophe());
      sootEntity.setROPHEp(1);
    }
    if (!Objects.equals(sootinDTO.getRoahe(), "--")) {
      sootEntity.setROAHE(sootinDTO.getRoahe());
      sootEntity.setROAHEp(1);
    }
    if (!Objects.equals(sootinDTO.getTvd(), "--")) {
      sootEntity.setTvd(sootinDTO.getTvd());
      sootEntity.setTvdp(1);
    }
    if (!Objects.equals(sootinDTO.getX(), "--")) {
      sootEntity.setX(sootinDTO.getX());
      sootEntity.setXp(1);
    }
    if (!Objects.equals(sootinDTO.getZeni(), "--")) {
      sootEntity.setZeni(sootinDTO.getZeni());
      sootEntity.setZenip(1);

    }
    sootRepository.save(sootEntity);
    List<CurveEntity> curveEntities = curveRepository.findAllByProjectAndIsSynthetic(project, false);
    Optional <CurveEntity> zeni = curveEntities.stream()
            .filter(curve -> Objects.equals(curve.getName(), sootEntity.getZeni()))
            .findFirst();
    if (sootEntity.getZenip() == 1 && zeni.isPresent()){

      TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};


      CurveEntity dept = curveEntities.stream()
              .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
              .findFirst().orElseThrow(
                      () -> new NoSuchElementException("123"));



      List<Double> curveData;
      CurveEntity p =new CurveEntity();

      for(CurveEntity curveEntity: curveEntities) {
        p.setData(curveEntity.getData());
        if(/*Objects.equals(curveEntity.getName(), sootEntity.getZeni()) ||*/
                Objects.equals(curveEntity.getName(), "DEPT")){
          continue;
        }
        boolean x = Objects.equals(curveEntity.getName(), sootEntity.getX());
        boolean tvd = Objects.equals(curveEntity.getName(), sootEntity.getTvd());

        //System.out.println(curveEntity.getName());
        curveData = interpolationService.extrapolateCurves(
                CurveDataDownloadResponse.builder()
                        .curveData(gson.fromJson(curveEntity
                                .getData(), floatListTypeToken))
                        .build().getCurveData().toArray(Double[]::new),

                CurveDataDownloadResponse.builder()
                        .curveData(gson.fromJson(dept
                                .getData(), floatListTypeToken))
                        .build().getCurveData().toArray(Double[]::new),
                x,
                tvd,
                CurveDataDownloadResponse.builder()
                        .curveData(gson.fromJson(zeni.get()
                                .getData(), floatListTypeToken))
                        .build().getCurveData().toArray(Double[]::new));
        //System.out.println(curveEntity.getName());

        curveEntity.setData(gson.toJson(curveData));
        //printCurves(curveEntity, p);
      }
      projectRepository.save(project);
      curvesService.updateTvdInProjectState(idProject, sootinDTO.getTvd());
      maxMinDTO = curvesService.getCurveMaxMin(sootinDTO.getTvd(), idProject);
    }

    if (project.getNewProject()!= null && project.getNewProject() && checkCurves(project)) {
      project.setNewProject(false);
      List<ModelEntity> modelEntitieList = project.getModelEntityList();
      Optional<ModelEntity> model = modelEntitieList.stream()
              .max(Comparator.comparing(ModelEntity::getEndX));
      if (model.isPresent()) {
        ModelDTO modelDTO = modelService.mapModelDto(model.get());
        modelService.saveModel(modelDTO, project);
      }
      TypeToken<List<Double>> floatListTypeToken = new TypeToken<>() {
      };
      /*for (CurveEntity curveEntity : project.getCurves()) {
        System.out.println(curveEntity.getName() + "    :" + CurveDataDownloadResponse.builder()
                .curveData(gson.fromJson(curveEntity
                        .getData(), floatListTypeToken))
                .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray().length);
      }*/
    }

    return maxMinDTO;
  }

  public SootoutDTO sootOut(Long idProject) {
    ProjectEntity project = projectRepository.findById(idProject)
        .orElseThrow(
            () -> new EntityNotFoundException("Проект не найден по указанному id: " + idProject));
    SootEntity sootEntity = project.getSootEntity();
    SootoutDTO sootoutDTO = new SootoutDTO();
    sootoutDTO.setROPL(sootEntity.getROPL());
    sootoutDTO.setROAL(sootEntity.getROAL());
    sootoutDTO.setROPLD(sootEntity.getROPLD());
    sootoutDTO.setROALD(sootEntity.getROALD());
    sootoutDTO.setROPLE(sootEntity.getROPLE());
    sootoutDTO.setROALE(sootEntity.getROALE());
    sootoutDTO.setROPH(sootEntity.getROPH());
    sootoutDTO.setROAH(sootEntity.getROAH());
    sootoutDTO.setROPHD(sootEntity.getROPHD());
    sootoutDTO.setROAHD(sootEntity.getROAHD());
    sootoutDTO.setROPHE(sootEntity.getROPHE());
    sootoutDTO.setROAHE(sootEntity.getROAHE());
    sootoutDTO.setMd(sootEntity.getMd());
    sootoutDTO.setTvd(sootEntity.getTvd());
    sootoutDTO.setX(sootEntity.getX());
    sootoutDTO.setZeni(sootEntity.getZeni());
    sootoutDTO.setROPLp(sootEntity.getROPLp());
    sootoutDTO.setROALp(sootEntity.getROALp());
    sootoutDTO.setROPLDp(sootEntity.getROPLDp());
    sootoutDTO.setROALDp(sootEntity.getROALDp());
    sootoutDTO.setROPLEp(sootEntity.getROPLEp());
    sootoutDTO.setROALEp(sootEntity.getROALEp());
    sootoutDTO.setROPHp(sootEntity.getROPHp());
    sootoutDTO.setROAHp(sootEntity.getROAHp());
    sootoutDTO.setROPHDp(sootEntity.getROPHDp());
    sootoutDTO.setROAHDp(sootEntity.getROAHDp());
    sootoutDTO.setROPHEp(sootEntity.getROPHEp());
    sootoutDTO.setROAHEp(sootEntity.getROAHEp());
    sootoutDTO.setMdp(sootEntity.getMdp());
    sootoutDTO.setTvdp(sootEntity.getTvdp());
    sootoutDTO.setXp(sootEntity.getXp());
    sootoutDTO.setZenip(sootEntity.getZenip());
    return sootoutDTO;
  }



  public void sootOffer(SaveCurveDataResponse lasFileUploadResponse, Long idProject) {
    SootEntity sootEntity = projectRepository.findById(idProject).
        orElseThrow(
            () -> new EntityNotFoundException("Проект не найден по указанному id: " + idProject)).
        getSootEntity();
    List<String> curves = lasFileUploadResponse.getCurvesNames();
    if (curves.contains("ROPL") && sootEntity.getROPLp() != 1) {
      sootEntity.setROPL("ROPL");
      sootEntity.setROPLp(2);
    }
    if (curves.contains("ROAL") && sootEntity.getROALp() != 1) {
      sootEntity.setROAL("ROAL");
      sootEntity.setROALp(2);
    }
    if (curves.contains("ROPLD") && sootEntity.getROPLDp() != 1) {
      sootEntity.setROPLD("ROPLD");
      sootEntity.setROPLDp(2);
    }
    if (curves.contains("ROALD") && sootEntity.getROALDp() != 1) {
      sootEntity.setROALD("ROALD");
      sootEntity.setROALDp(2);
    }
    if (curves.contains("ROPLE") && sootEntity.getROPLEp() != 1) {
      sootEntity.setROPLE("ROPLE");
      sootEntity.setROPLEp(2);
    }
    if (curves.contains("ROALE") && sootEntity.getROALEp() != 1) {
      sootEntity.setROALE("ROALE");
      sootEntity.setROALEp(2);
    }
    if (curves.contains("ROPH") && sootEntity.getROPHp() != 1) {
      sootEntity.setROPH("ROPH");
      sootEntity.setROPHp(2);
    }
    if (curves.contains("ROAH") && sootEntity.getROAHp() != 1) {
      sootEntity.setROAH("ROAH");
      sootEntity.setROAHp(2);
    }
    if (curves.contains("ROPHD") && sootEntity.getROPHDp() != 1) {
      sootEntity.setROPHD("ROPHD");
      sootEntity.setROPHDp(2);
    }
    if (curves.contains("ROAHD") && sootEntity.getROAHDp() != 1) {
      sootEntity.setROAHD("ROAHD");
      sootEntity.setROAHDp(2);
    }
    if (curves.contains("ROPHE") && sootEntity.getROPHEp() != 1) {
      sootEntity.setROPHE("ROPHE");
      sootEntity.setROPHEp(2);
    }
    if (curves.contains("ROAHE") && sootEntity.getROAHEp() != 1) {
      sootEntity.setROAHE("ROAHE");
      sootEntity.setROAHEp(2);
    }
    if (curves.contains("TVD") && sootEntity.getTvdp() != 1) {
      sootEntity.setTvd("TVD");
      sootEntity.setTvdp(2);
    }
    if (curves.contains("X") && sootEntity.getXp() != 1) {
      sootEntity.setX("X");
      sootEntity.setXp(2);
    }
    if (curves.contains("zeni") && sootEntity.getZenip() != 1) {
      sootEntity.setZeni("zeni");
      sootEntity.setZenip(2);
    }

    if (curves.contains("tvd") && sootEntity.getTvdp() != 1) {
      sootEntity.setTvd("tvd");
      sootEntity.setTvdp(2);
    }
    if (curves.contains("x") && sootEntity.getXp() != 1) {
      sootEntity.setX("x");
      sootEntity.setXp(2);
    }
    if (curves.contains("ZENI") && sootEntity.getZenip() != 1) {
      sootEntity.setZeni("ZENI");
      sootEntity.setZenip(2);
    }
    sootRepository.save(sootEntity);
  }

  public Boolean checkCurves(Long projectId) {
    ProjectEntity projectEntity = projectRepository.findById(projectId).
        orElseThrow(
            () -> new EntityNotFoundException("Проект не найден по указанному id: " + projectId));
    return checkCurves(projectEntity);
  }

  public Boolean checkCurves(ProjectEntity projectEntity) {
    SootEntity sootEntity = projectEntity.getSootEntity();
    if (sootEntity.getZenip() != 1 || sootEntity.getXp() != 1 || sootEntity.getTvdp() != 1 ) {
      return false;
    }

    return sootEntity.getROPLp() == 1 ||
            sootEntity.getROALp() == 1 ||
            sootEntity.getROPLDp() == 1 ||
            sootEntity.getROALDp() == 1 ||
            sootEntity.getROPLEp() == 1 ||
            sootEntity.getROALEp() == 1 ||
            sootEntity.getROPHp() == 1 ||
            sootEntity.getROAHp() == 1 ||
            sootEntity.getROPHDp() == 1 ||
            sootEntity.getROAHDp() == 1 ||
            sootEntity.getROPHEp() == 1 ||
            sootEntity.getROAHEp() == 1;
  }
}
