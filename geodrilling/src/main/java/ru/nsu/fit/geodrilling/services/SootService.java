package ru.nsu.fit.geodrilling.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.geodrilling.dto.SootinDTO;
import ru.nsu.fit.geodrilling.dto.SootoutDTO;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.SootEntity;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;
import ru.nsu.fit.geodrilling.repositories.SootRepository;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SootService {
    private ProjectRepository projectRepository;
    private SootRepository sootRepository;
    public String sootRename(Long idProject, SootinDTO sootinDTO) {
        ProjectEntity project = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден по указанному id: " + idProject));
        SootEntity sootEntity = project.getSootEntity();
        if (sootinDTO.getRopl() != "--") {
            sootEntity.setROPL(sootinDTO.getRopl());
            sootEntity.setROPLp(1);
        }
        if (sootinDTO.getRoal() != "--") {
            sootEntity.setROAL(sootinDTO.getRoal());
            sootEntity.setROALp(1);
        }
        if (sootinDTO.getRopld() != "--") {
            sootEntity.setROPLD(sootinDTO.getRopld());
            sootEntity.setROPLDp(1);
        }
        if (sootinDTO.getRoald() != "--") {
            sootEntity.setROALD(sootinDTO.getRoald());
            sootEntity.setROALDp(1);
        }
        if (sootinDTO.getRople() != "--") {
            sootEntity.setROPLE(sootinDTO.getRople());
            sootEntity.setROPLEp(1);
        }
        if (sootinDTO.getRoale() != "--") {
            sootEntity.setROALE(sootinDTO.getRoale());
            sootEntity.setROALEp(1);
        }
        if (sootinDTO.getRoph() != "--") {
            sootEntity.setROPH(sootinDTO.getRoph());
            sootEntity.setROPHp(1);
        }
        if (sootinDTO.getRoah() != "--") {
            sootEntity.setROAH(sootinDTO.getRoah());
            sootEntity.setROAHp(1);
        }
        if (sootinDTO.getRophd() != "--") {
            sootEntity.setROPHD(sootinDTO.getRophd());
            sootEntity.setROPHDp(1);
        }
        if (sootinDTO.getRoahd() != "--") {
            sootEntity.setROAHD(sootinDTO.getRoahd());
            sootEntity.setROAHDp(1);
        }
        if (sootinDTO.getRophe() != "--") {
            sootEntity.setROPHE(sootinDTO.getRophe());
            sootEntity.setROPHEp(1);
        }
        if (sootinDTO.getRoahe() != "--") {
            sootEntity.setROAHE(sootinDTO.getRoahe());
            sootEntity.setROAHEp(1);
        }
        if (sootinDTO.getMd() != "--") {
            sootEntity.setMd(sootinDTO.getMd());
            sootEntity.setMdp(1);
        }
        if (sootinDTO.getTvd() != "--") {
            sootEntity.setTvd(sootinDTO.getTvd());
            sootEntity.setTvdp(1);
        }
        if (sootinDTO.getX() != "--") {
            sootEntity.setX(sootinDTO.getX());
            sootEntity.setXp(1);
        }
        if (sootinDTO.getZeni() != "--") {
            sootEntity.setZeni(sootinDTO.getZeni());
            sootEntity.setZenip(1);
        }
        sootRepository.save(sootEntity);
        return "ok";
    }

    public SootoutDTO sootOut(Long idProject) {
        ProjectEntity project = projectRepository.findById(idProject)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден по указанному id: " + idProject));
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
}
