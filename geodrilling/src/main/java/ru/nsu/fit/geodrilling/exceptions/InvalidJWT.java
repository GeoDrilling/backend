package ru.nsu.fit.geodrilling.exceptions;

public class InvalidJWT extends RuntimeException{

  public InvalidJWT(String message) {
    super(message);
  }
}
