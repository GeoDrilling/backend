package ru.nsu.fit.geodrilling.controllers;

import java.util.HashMap;
import java.util.Map;

import grillid9.laslib.exceptions.LogsReadingException;
import grillid9.laslib.exceptions.ReadWrapException;
import grillid9.laslib.exceptions.VersionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nsu.fit.geodrilling.exceptions.InvalidJWT;
import ru.nsu.fit.geodrilling.exceptions.LoginException;
import ru.nsu.fit.geodrilling.model.ExceptionMessage;

@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UsernameNotFoundException.class, LoginException.class, AuthenticationException.class})
  public ExceptionMessage handleSimpleExceptions(RuntimeException ex) {
    return new ExceptionMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) //406
  @ExceptionHandler({InvalidJWT.class})
  public ExceptionMessage handleInvalidJwtExceptions(RuntimeException ex) {
    return new ExceptionMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ExceptionMessage handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    return new ExceptionMessage("Required request body is missing");
  }

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler({VersionException.class, ReadWrapException.class, LogsReadingException.class})
  public ExceptionMessage handleVersionException(RuntimeException ex) {
    return new ExceptionMessage(ex.getMessage());
  }
}
