package ru.nsu.fit.geodrilling.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  @Email(message = "Incorrect email")
  @NotBlank(message = "Email cannot be empty")
  private String email;
  private String name;
  private Long id;
}
