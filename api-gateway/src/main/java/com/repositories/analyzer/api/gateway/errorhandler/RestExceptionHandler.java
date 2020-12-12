package com.repositories.analyzer.api.gateway.errorhandler;

import com.repositories.analyzer.common.api.model.GenericResponse;
import com.repositories.analyzer.common.usecase.CommonFailures;
import com.repositories.analyzer.common.usecase.FailureDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> constraintViolationFailed(final ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(
                new GenericResponse(
                        ex.getConstraintViolations().stream()
                                .map(violation -> new FailureDto(violation.getPropertyPath().toString(), violation.getMessage()))
                                .collect(Collectors.toUnmodifiableList())
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> onException(final Exception ex) {
        logger.error("Exception was thrown when processing the request.", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GenericResponse(
                        List.of(
                                new FailureDto(
                                        CommonFailures.INTERNAL_SERVER_ERROR.code(),
                                        CommonFailures.INTERNAL_SERVER_ERROR.reason()
                                )
                        )
                )
        );
    }
}