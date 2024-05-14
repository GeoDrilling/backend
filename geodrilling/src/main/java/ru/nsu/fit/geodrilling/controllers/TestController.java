package ru.nsu.fit.geodrilling.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.InterpolateDTO;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.services.InterpolationService;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
  private final ModelMapper modelMapper;
  private final NativeLibrary nativeLibrary;
  private final InterpolationService interpolationService;
  @GetMapping("/demo")
  public UserDTO demo() {
    log.info("in demo");
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

    return modelMapper.map((token.getPrincipal()), UserDTO.class);
  }

  @GetMapping("/inter")
  public InterpolateDTO inter() {
    double[] a = {4, 20};
    List<double[]> aa = new ArrayList<>();
    aa.add(new double[]{1, 2});
    aa.add(new double[]{3, 4});
    List<double[]> bb = new ArrayList<>();
    bb.add(new double[]{1, 2, 3, 4, 5, 6, 7});
    bb.add(new double[]{1, 2, 3, 4, 5, 6, 7});
    double[] b = {1, 2, 3, 4, 5, 6, 7};

    return interpolationService.interpolateDepths(a, aa, b, bb);
  }
  @GetMapping("/syn")
  public List<Double> syn() {
    double[] a = {4, 20, 1, 2, 3, 4, 5};
    double[] b = {1, 2, 3, 4, 5, 6, 7};
    double[] bb = {0, 1.5, 2.1, 3.2, 4.3, 5.4, 6.8,7.5, 8, 9, 10};

    return interpolationService.interpolateSynthetic(b, a, bb);
  }
/*  @GetMapping("/lib")
  public ResponseEntity<OutputModel> demoLib() {
    double[] x = { 0 };
    int size_indata = 2850;
    double[] tvd = new double[size_indata];
    fill(tvd, size_indata, 3);
    double[] zeni = new double[size_indata];
    fill(zeni, size_indata, 90);
    double[] phases = new double[size_indata * 6];
    fill(phases, 6 * size_indata, 1);
    double[] ampl = new double[size_indata * 6];
    fill(ampl, 6 * size_indata, 0.85);
    double[] md = new double[size_indata];
    for (int i = 0; i < size_indata; i++) md[i] = i * 0.1;
    double tvdstart = 0;
    double alpha = 0;
    double rof = 0;
    double rosf = 0;
    double kf = 0;
    double ksf = 0;
    int[] num_probe = { 1039, 1040, 1041, 1042, 1043, 1044 };
    OutputModel model = nativeLibrary.startModel(new InputBuildModel(6, num_probe, size_indata, md, tvd, x, zeni, phases, ampl,
        tvdstart, 0, 0,
        alpha, 0, 0,
        rof,
        kf,
        rosf,
        ksf));
    return ResponseEntity.ok(model);
  }
  private void fill(double[] arr, int size, double value) {
    for (int i = 0; i < size; i++)
      arr[i] = value;
  }*/
}
