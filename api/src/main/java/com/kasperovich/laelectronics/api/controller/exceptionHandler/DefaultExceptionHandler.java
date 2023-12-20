package com.kasperovich.laelectronics.api.controller.exceptionHandler;

import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.exception.PreconditionException;
import com.kasperovich.laelectronics.util.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class

DefaultExceptionHandler {
    String strErr="error";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, ErrorContainer>> allException(Exception exception){
        return new ResponseEntity<>(Collections.singletonMap(strErr, ErrorContainer
                .builder()
                .exceptionId(UUIDGenerator.generateUUID())
                .errorCode(1)
                .errorMessage("General Error")
                .e(exception.getClass().toString())
                .build()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            BadPasswordException.class,
            NotDeletableStatusException.class,
            NumberFormatException.class,
    })
    public ResponseEntity<Map<String, ErrorContainer>> handledException(Exception exception){
        return new ResponseEntity<>(Collections.singletonMap(strErr, ErrorContainer
                .builder()
                .exceptionId(UUIDGenerator.generateUUID())
                .errorCode(5)
                .errorMessage(exception.getMessage())
                .e(exception.getClass().toString())
                .build()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            NoSuchElementException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<Object> handlerEntityNotFoundException(Exception e) {

        ErrorContainer error =
                ErrorContainer.builder()
                        .exceptionId(UUIDGenerator.generateUUID())
                        .errorCode(2)
                        .errorMessage(e.getMessage())
                        .e(e.getClass().toString())
                        .build();
        return new ResponseEntity<>(Collections.singletonMap(e, error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handlerDataIntegrityViolationException(Exception e) {

        ErrorContainer error =
                ErrorContainer.builder()
                        .exceptionId(UUIDGenerator.generateUUID())
                        .errorCode(4)
                        .errorMessage("Data integrity violation")
                        .e(e.getClass().toString())
                        .build();
        return new ResponseEntity<>(Collections.singletonMap("Error:",error),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handlerAccessDenied(Exception e) {

        ErrorContainer error =
                ErrorContainer.builder()
                        .exceptionId(UUIDGenerator.generateUUID())
                        .errorCode(3)
                        .errorMessage("Access denied")
                        .e(e.getClass().toString())
                        .build();
        return new ResponseEntity<>(Collections.singletonMap("Error:",error),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handlerBadCredentials(Exception e) {

        ErrorContainer error =
                ErrorContainer.builder()
                        .exceptionId(UUIDGenerator.generateUUID())
                        .errorCode(6)
                        .errorMessage("Incorrect data!")
                        .e(e.getClass().toString())
                        .build();
        return new ResponseEntity<>(Collections.singletonMap("Error:",error),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PreconditionException.class)
    public ResponseEntity<Object> handlerPrecondition(PreconditionException e) {
        ErrorContainer error =
                ErrorContainer.builder()
                        .exceptionId(UUIDGenerator.generateUUID())
                        .errorCode(7)
                        .errorMessage(e.getMessage())
                        .e(e.getClass().toString())
                        .build();
        return new ResponseEntity<>(Collections.singletonMap("Error:",error),HttpStatus.PRECONDITION_FAILED);
    }

}
