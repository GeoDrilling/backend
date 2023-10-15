package ru.nsu.fit.geodrilling.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.geodrilling.dto.InputBuildModel;
import ru.nsu.fit.geodrilling.dto.UserDTO;
import ru.nsu.fit.geodrilling.model.Constant;
import ru.nsu.fit.geodrilling.model.OutputModel;
import ru.nsu.fit.geodrilling.services.lib.NativeLibrary;

@RestController
@RequiredArgsConstructor
public class TestController {
  private final ModelMapper modelMapper;
  private final NativeLibrary nativeLibrary;
  @GetMapping("/demo")
  public UserDTO demo() {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    return modelMapper.map((token.getPrincipal()), UserDTO.class);
  }

  @GetMapping("/lib")
  public int demoLib() {
    double[] x = { 0 };
    int size_indata = 1024;
    double[] tvd = new double[size_indata];
    fill(tvd, size_indata, 3);
    double[] zeni = new double[size_indata];
    fill(zeni, size_indata, 90);
    double[] phases = new double[size_indata * 2];
    fill(phases, 2 * size_indata, 1);
    double[] ampl = new double[size_indata * 2];
    fill(ampl, 2 * size_indata, 0.85);
    double[] md = new double[size_indata];
    for (int i = 0; i < size_indata; i++) md[i] = i * 0.1;
    double tvdstart = 0;
    double alpha = 0;
    double rof = 0;
    double rosf = 0;
    double kf = 0;
    double ksf = 0;
    int[] num_probe = { Constant.LWD_LOOCH_VIKPB_ROL, Constant.LWD_LOOCH_VIKPB_ROH };
    OutputModel model = nativeLibrary.startModel(new InputBuildModel(2, num_probe, size_indata, md, tvd, x, zeni, phases, ampl,
        tvdstart, -1, -1,
        alpha, 0, 10,
        rof,
        kf,
        rosf,
        ksf));
    return model.getStatus();
  }
  private void fill(double[] arr, int size, double value) {
    for (int i = 0; i < size; i++)
      arr[i] = value;
  }
}
