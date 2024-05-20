package project.infrastructure.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ControllerAdvice {
  @ExceptionHandler(ResponseStatusException.class)
  protected ResponseEntity<String> handleException(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
  }
}
