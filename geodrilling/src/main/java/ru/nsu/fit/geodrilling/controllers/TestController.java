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
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
  private final ModelMapper modelMapper;
  private final NativeLibrary nativeLibrary;
  @GetMapping("/demo")
  public UserDTO demo() {
    log.info("in demo");
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

    return modelMapper.map((token.getPrincipal()), UserDTO.class);
  }

  @GetMapping("/lib")
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
  }
}
