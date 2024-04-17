package ru.nsu.fit.geodrilling.config;

import com.google.gson.Gson;
import jakarta.servlet.MultipartConfigElement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.unit.DataSize;
import ru.nsu.fit.geodrilling.advice.ProjectAspect;
import ru.nsu.fit.geodrilling.repositories.ProjectRepository;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig {
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
  public Gson gson() {
    return new Gson();
  }
}
