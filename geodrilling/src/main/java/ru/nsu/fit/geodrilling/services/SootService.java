package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.MaxMinDTO;
import ru.nsu.fit.geodrilling.dto.SootinDTO;
import ru.nsu.fit.geodrilling.dto.SootoutDTO;
import ru.nsu.fit.geodrilling.dto.curves.LasFileUploadResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SootService {

  private ProjectRepository projectRepository;
  private SootRepository sootRepository;
  private CurvesService curvesService;

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
      curvesService.updateTvdInProjectState(idProject, sootinDTO.getTvd());
      maxMinDTO = curvesService.getCurveMaxMin(sootinDTO.getTvd(), idProject);
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
    sootRepository.save(sootEntity);
  }

  public Boolean checkCurves(Long projectId) {
    ProjectEntity projectEntity = projectRepository.findById(projectId).
        orElseThrow(
            () -> new EntityNotFoundException("Проект не найден по указанному id: " + projectId));
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
