package ru.nsu.fit.geodrilling.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @Email(message = "Incorrect email")
  @NotBlank(message = "Email cannot be empty")
  private String email;
  @NotBlank(message = "Password should not be empty")
  @Size(min = 4, max = 32, message = "Password must be between 4 and 32 characters long")
  private String password;
}
