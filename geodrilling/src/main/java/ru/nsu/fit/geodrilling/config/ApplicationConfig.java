package ru.nsu.fit.geodrilling.config;

import jakarta.servlet.MultipartConfigElement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class ApplicationConfig {

  @Value("${projects.folder-path}")
  public String projectsDir;

  @Value("${lasfile.temp-path}")
  public String tempDir;
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();

    factory.setMaxFileSize(DataSize.ofMegabytes(10));
    factory.setMaxRequestSize(DataSize.ofMegabytes(10));
    return factory.createMultipartConfig();
  }



  @Bean
  public CommandLineRunner dirInitializer() {
    return args -> {
      Files.createDirectories(Path.of(projectsDir));
      Files.createDirectories(Path.of(tempDir));
    };
  }
}
