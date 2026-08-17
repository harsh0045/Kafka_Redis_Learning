package com.example.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * 404 - Product Not Found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }


    /*
     * 400 - DTO Validation Error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField()
                                + ": "
                                + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }


    /*
     * 500 - Unexpected Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(
            InsufficientStockException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        400,
                        ex.getMessage(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}