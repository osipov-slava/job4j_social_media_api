package ru.job4j.socialmediaapi.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.job4j.socialmediaapi.validation.ValidationErrorResponse;
import ru.job4j.socialmediaapi.validation.Violation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();
        log.error(e.getLocalizedMessage());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public void catchDataIntegrityViolationException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, String> details = new HashMap<>();
        details.put("message", e.getMessage());
        details.put("type", String.valueOf(e.getClass()));
        details.put("timestamp", String.valueOf(LocalDateTime.now()));
        details.put("path", request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(details));
        log.error(e.getLocalizedMessage());
    }

}
