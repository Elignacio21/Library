package com.github.Ignacio.my_Library_In_Spring.HandingError;

import com.github.Ignacio.my_Library_In_Spring.DTOs.ApiError;

import com.github.Ignacio.my_Library_In_Spring.DTOs.ValidationDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException ex, HttpServletRequest request ){

        ApiError result = new ApiError(
                HttpStatus.NOT_FOUND.value()
                ,"Not Found"
                ,ex.getMessage()
                , request.getRequestURI()
                , LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex
            , WebRequest request){

            List<ValidationDetail> details = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(detail -> new ValidationDetail(detail.getField(), detail.getCode(), detail.getDefaultMessage()))
                    .toList();

            String uri = request.getDescription(true)
                    .replace("uri?","");

            ApiError result = new ApiError(HttpStatus.BAD_REQUEST.value()
                    ,"BAD REQUEST"
                    ,ex.getMessage()
                    ,uri
                    ,LocalDateTime.now()
                    ,details);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(NotAuthorAvailableException.class)
    public ResponseEntity<?> notAuthorAvailableException(NotAuthorAvailableException ex,WebRequest request){
        String uri = request.getDescription(false).replace("uri=","");

        ApiError result = new ApiError(HttpStatus.NO_CONTENT.value(), "Not Content",ex.getMessage(),uri,LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }

    @ExceptionHandler(NotBooksAvailableException.class)
    public ResponseEntity<?> notBooksAvailableException(NotBooksAvailableException ex,WebRequest request){
        String uri = request.getDescription(false).replace("uri=","");

        ApiError result = new ApiError(HttpStatus.NO_CONTENT.value(), "Not Content",ex.getMessage(),uri,LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex,WebRequest request){
        String uri = request.getDescription(false).replace("uri=","");

        ApiError result = new ApiError(HttpStatus.FORBIDDEN.value()
                ,"Forbidden"
                ,"You do not have permission to access this resource."
                , uri,LocalDateTime.now() );


        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex,WebRequest request){
        String uri = request.getDescription(false).replace("uri=", "");

        List<ValidationDetail> details = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationDetail(
                        violation.getPropertyPath().toString(),   // campo que falló
                        violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(), // tipo de validación
                        violation.getMessage()                    // mensaje
                ))
                .toList();

        ApiError apiError = new ApiError(
                400,
                "ValidationError",
                "Validation failed",
                uri,
                LocalDateTime.now(),
                details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);


    }
}
