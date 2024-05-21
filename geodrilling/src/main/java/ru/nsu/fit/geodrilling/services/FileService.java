package ru.nsu.fit.geodrilling.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import grillid9.laslib.LasReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;
import ru.nsu.fit.geodrilling.dto.ProjectStateDTO;
import ru.nsu.fit.geodrilling.dto.curves.CurveDataDownloadResponse;
import ru.nsu.fit.geodrilling.dto.curves.SaveCurveDataResponse;
import ru.nsu.fit.geodrilling.entity.CurveEntity;
import ru.nsu.fit.geodrilling.entity.ModelEntity;
import ru.nsu.fit.geodrilling.entity.ProjectEntity;
import ru.nsu.fit.geodrilling.entity.projectstate.TrackProperty;
import ru.nsu.fit.geodrilling.repositories.CurveRepository;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final Gson gson;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final CurvesService curvesService;
    private final InterpolationService interpolationService;
    private final ModelService modelService;
    private final CurveRepository curveRepository;

    /*public  void printCurves(CurveEntity zeni1, CurveEntity zeni2) {
        // Извлекаем и преобразуем данные первой кривой
        Double[] curve1 = extractCurve(zeni1);
        // Извлекаем и преобразуем данные второй кривой
        Double[] curve2 = extractCurve(zeni2);
        System.out.println(zeni1.getName()+   "     "  +zeni2.getName());
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

    @Transactional
    public SaveCurveDataResponse save(MultipartFile file, Long projectId) throws IOException {
        log.info("Добавление кривых в проект id={}", projectId);
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(()
                -> new NoSuchElementException("Проект с id " + projectId + " не существует"));
        List<CurveEntity> curves;
        CurveEntity newDepthCurve;
        if (file.getContentType().equals("text/csv")) {
            curves = fromCsv(file);
            newDepthCurve = curves.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "MD"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("Кривой DEPT не существует во входящем файле"));
            newDepthCurve.setName("DEPT");

        } else if (file.getContentType().equals("application/octet-stream")) {
            curves = fromLas(file);
            newDepthCurve = curves.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("Кривой DEPT не существует во входящем файле"));
        } else {
            log.error("Расширение файла {} не поддерживается!", file.getContentType());
            throw new InvalidContentTypeException("Расширение файла " + file.getContentType() + " не поддерживается!");
        }
        log.info("123");
        List<CurveEntity> curvesInProject = curveRepository.findAllByProjectAndIsSynthetic(project, false);
        // если в проекте уже есть кривые, происходит склейка по кривой DEPT
        if (!curvesInProject.isEmpty()) {
            log.info("Склейка кривых в проекте id={}", projectId);

//            String depthInProjectData = gson.toJson(getCurveDataByName("DEPT", project.getId(), false).getCurveData());
//            if (!depthInProjectData.equals(newDepthCurve.getData())) {
//                throw new NewCurvesAddingException("Кривая DEPT не соответствует уже добалвенной");
//            }

            //nik
            /*for (CurveEntity с : curves) {
                System.out.println(newDepthCurve.getName());
            }*/


            TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};

            double[] DeptNewInArray = CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(newDepthCurve
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray();

            curves.remove(newDepthCurve);



            CurveEntity Dept = curvesInProject.stream()
                    .filter(curve -> Objects.equals(curve.getName(), "DEPT"))
                    .findFirst().orElseThrow(
                            () -> new NoSuchElementException("123"));

            double[] DeptInArray = CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(Dept
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray();

            curvesInProject.remove(Dept);

            Iterator<CurveEntity> iterator = curves.iterator();
            while (iterator.hasNext()) {
                CurveEntity c = iterator.next();
                // Проверяем, существует ли уже кривая с таким именем в данном проекте
                Optional<CurveEntity> existingCurve = curvesInProject.stream()
                        .filter(curve -> Objects.equals(curve.getName(), c.getName())).findAny();
                if (existingCurve.isPresent()) {
                    // Логируем информацию о том, что кривая уже существует
                    log.info("Кривая с именем " + c.getName() + " уже существует в проекте. Она будет удалена из списка.");
                    // Удаление кривой из списка
                    iterator.remove();
                }
            }

            List<String> names = new ArrayList<>();
            for (CurveEntity curve :  curves){
                names.add(curve.getName());
            }

            for (CurveEntity curve :  curvesInProject){
                names.add(curve.getName());
            }

            /*for (String name :  names){
                System.out.println(name);
            }
*/

            List<double []> curvesInProjectInArray =   curvesInProject.stream()
                    .map(list -> CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(list
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream()
                            .mapToDouble(Double::doubleValue)
                            .toArray())
                    .collect(Collectors.toList());


            List<double []> curvesNewInArray =   curves.stream()
                    .map(list -> CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(list
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream()
                            .mapToDouble(Double::doubleValue)
                            .toArray())
                    .collect(Collectors.toList());
           /* System.out.println(DeptInArray.length);
            System.out.println(DeptNewInArray.length);
            System.out.println(curvesNewInArray.size());
            System.out.println(curvesNewInArray.get(0).length);
            System.out.println(curvesInProjectInArray.size());
            System.out.println(curvesInProjectInArray.get(0).length);*/
            InterpolateDTO interpolateDTO = interpolationService.interpolateDepths(DeptNewInArray, curvesNewInArray, DeptInArray, curvesInProjectInArray);

            List <CurveEntity> curveS = curveRepository.findAllByProjectAndIsSynthetic(project, true);
            List <CurveEntity> curveNewS = new ArrayList<>();
            for (CurveEntity c : curveS) {
                curveNewS.add(new CurveEntity(null, project, c.getName(), gson.toJson(
                interpolationService.interpolateSynthetic(DeptInArray, CurveDataDownloadResponse.builder()
                        .curveData(gson.fromJson(c
                                .getData(), floatListTypeToken))
                        .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray(), interpolateDTO.depth.stream().mapToDouble(Double::doubleValue).toArray()) )
                        , "synthetic/", true) );
            }
            project.getCurves().clear();
            curves.clear();

            curves.add(new CurveEntity(null, project, "DEPT", gson.toJson(interpolateDTO.getDepth()), "", false));
            for(int i =0; i < interpolateDTO.getCurves().size(); i++){
                curves.add(new CurveEntity(null, project, names.get(i), gson.toJson(interpolateDTO.getCurves().get(i)), "", false));
            }
            curves.addAll(curveNewS);
        }

        for (CurveEntity curve : curves) {
            curve.setProject(project);
            project.getCurves().add(curveRepository.save(curve));
        }
        log.info("Кривые добавлены в проект");
        projectRepository.save(project);
        try {
           curvesService.updateDeptInProjectState(projectId);
        } catch (Exception e) {
            log.warn("Кривой DEPT нет в добавленных кривых");
        }

        curvesService.handleMultiCurves(project);
        return SaveCurveDataResponse.builder()
                .curvesNames(curvesService.getCurvesNames(projectId).getCurvesNames())
                .build();
    }

    @Transactional
    public ProjectStateDTO supplementCurve(MultipartFile file, Long projectId) throws IOException {
        log.info("Проект " + projectId + ": дополнение кривых");
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Проект не найден"));
        ProjectEntity newProject = projectRepository.findById(projectService
                        .createProjectForUser(project.getUser().getEmail(), project.getName()).getId())
                .orElseThrow(() -> new NoSuchElementException("Не найден новый проект"));
        newProject.setNewProject(true);
        save(file, newProject.getId());
        log.info("Дополненные кривые сохранены в новый проект с id {}", newProject.getId());
        project.setReadOnly(true);
        projectRepository.save(project);
        log.info("Проект {} заморожен", project.getId());
        project.setSupplementingProject(newProject);
        List<ModelEntity> newModelList =new ArrayList<>();
        for(ModelEntity model: project.getModelEntityList()){
            ModelEntity l = new ModelEntity(model);
            l.setProjectEntity(newProject);
            newModelList.add(l);
        }
        Optional<ModelEntity> model = newModelList.stream()
                .max(Comparator.comparing(ModelEntity::getEndX));
        if (model.isPresent()) {
            model.get().setEndX(curvesService.getDeptMax(newProject.getId()));
        }
        for(ModelEntity m: newModelList){
            newProject.getModelEntityList().add(m);
        }
        CurveEntity dept = curveRepository.findByNameAndProjectAndIsSynthetic("DEPT", project, false).get();
        CurveEntity deptNew = curveRepository.findByNameAndProjectAndIsSynthetic("DEPT", newProject, false).get();
        List<CurveEntity> curveS = curveRepository.findAllByProjectAndIsSynthetic(project, true);
        TypeToken<List<Double>> floatListTypeToken = new TypeToken<>(){};
        System.out.println();
        for(CurveEntity c : curveS){
            /*System.out.println(CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(dept
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray().length);
            System.out.println( CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(c
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray().length);
            System.out.println(CurveDataDownloadResponse.builder()
                    .curveData(gson.fromJson(deptNew
                            .getData(), floatListTypeToken))
                    .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray().length);*/
            List<Double> data = interpolationService.interpolateSynthetic(
                    CurveDataDownloadResponse.builder()
                        .curveData(gson.fromJson(dept
                                .getData(), floatListTypeToken))
                         .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray(),

                    CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(c
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray(),

                    CurveDataDownloadResponse.builder()
                            .curveData(gson.fromJson(deptNew
                                    .getData(), floatListTypeToken))
                            .build().getCurveData().stream().mapToDouble(Double::doubleValue).toArray()

            );
            newProject.getCurves().add(new CurveEntity(null, newProject, c.getName(), gson.toJson(data), "synthetic/", true));
        }
//        newProject.getState().getTabletProperties().getProperties().addAll(project.getState().getTabletProperties().getProperties());
        newProject.getState().setTrackProperties(new ArrayList<>());
        newProject.getState().getTrackProperties().addAll(
                project.getState().getTrackProperties().stream()
                        .map(c -> {

                            TrackProperty copy = new TrackProperty();
                            copy.setProperties(new ArrayList<>(c.getProperties()));
                            copy.setCurves(new ArrayList<>(c.getCurves()));  // Создаем новую копию списка кривых
                            return copy;  // Возвращаем копию объекта TrackProperty
                        })
                        .collect(Collectors.toList())
        );
        List<String> names = curvesService.getCurvesNames(newProject.getId()).getCurvesNames();
        for (TrackProperty trackProperty : newProject.getState().getTrackProperties()){
            trackProperty.setProperties(trackProperty.getProperties().stream().filter( c -> names.contains( c.getName())).collect(Collectors.toList()));
        }
        newProject.getState().setTrackProperties(newProject.getState().getTrackProperties().stream().filter(c -> ! c.getCurves().isEmpty()).collect(Collectors.toList()));
        projectRepository.save(newProject);
        return projectService.getProjectState(newProject.getId());
    }

    public List<CurveEntity> fromLas(MultipartFile file) {
        try {
            Path tempPath = Files.createTempFile(null, null);
            file.transferTo(tempPath);
            LasReader lasReader = new LasReader(tempPath.toFile().getAbsolutePath());
            lasReader.read();
            return lasReader.getCurves()
                    .stream()
                    .map(x -> CurveEntity.builder().name(x.getName()).data(gson.toJson(x.getData())).build())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<CurveEntity> fromCsv(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] curvesNames = reader.readNext();
            Map<String, List<Double>> nameToData  = new HashMap<>();
            for (String curveName : curvesNames) {
                nameToData.put(curveName, new ArrayList<>());
            }
            String[] dataLine;
            while ((dataLine = reader.readNext()) != null) {
                for (int i = 0; i < curvesNames.length; i++) {
                    nameToData.get(curvesNames[i]).add(Double.parseDouble(dataLine[i]));
                }
            }
            List<CurveEntity> curves = new ArrayList<>(curvesNames.length);
            for (int i = 0; i < curvesNames.length; i++) {
                curves.add(CurveEntity.builder().name(curvesNames[i]).data(gson.toJson(nameToData.get(curvesNames[i]))).build());
            }
            return curves;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }



}
