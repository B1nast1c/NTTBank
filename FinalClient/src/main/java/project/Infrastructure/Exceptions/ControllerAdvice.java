package project.Infrastructure.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<String> handleException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
    }
}
