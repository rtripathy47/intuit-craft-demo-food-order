package com.food.order.system.exception;


import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    
    @ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<?> insufficientCapacityException(InsufficientCapacityException ex) {
    	IssueDataModel genericErrorModel= new IssueDataModel();
    	genericErrorModel.setReason(ex.getMessage());
        return new ResponseEntity<>(genericErrorModel, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle all exceptions.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<IssueDataModel> handleAllExceptions(final Exception exception) {
        IssueDataModel genericErrorModel= new IssueDataModel();
        if (null != exception.getMessage()) {
        	genericErrorModel.setReason(exception.getMessage());
        } else {
        	genericErrorModel.setReason("Internal Server Error");
        }
        logErrorMessage(exception);
        return new ResponseEntity<>(genericErrorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Reporting service exception.
     *
     * @param reportingServiceException the reporting service exception
     * @return the response entity
     */
    @ExceptionHandler(value = { FoodOrderSystemException.class })
    public final ResponseEntity<IssueDataModel> handleReportingServiceException(
                                    final FoodOrderSystemException foodOrderSystemException) {
    	IssueDataModel genericErrorModel= new IssueDataModel();
    	genericErrorModel.setReason(foodOrderSystemException.getMessage());
        logErrorMessage(foodOrderSystemException);
        return new ResponseEntity<>(genericErrorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To handle input validations
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<IssueDataModel> constraintViolationException(ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final String contraintViolationErrorMessage = violations.stream().map(ConstraintViolation::getMessageTemplate)
                                        .collect(Collectors.joining(","));
        IssueDataModel genericErrorModel= new IssueDataModel();
    	genericErrorModel.setReason(contraintViolationErrorMessage);
        return new ResponseEntity<>(genericErrorModel, HttpStatus.BAD_REQUEST);
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.
     * ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.
     * springframework.web.bind.MethodArgumentNotValidException,
     * org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
     * org.springframework.web.context.request.WebRequest)
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                    HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IssueDataModel genericErrorModel= new IssueDataModel();
        final String methodArgsNotvalidErrorMessage = exception.getBindingResult().getFieldErrors().stream()
                                        .map(FieldError::getDefaultMessage)
                                        .collect(Collectors.joining(","));
        genericErrorModel.setReason(methodArgsNotvalidErrorMessage);
        logErrorMessage(methodArgsNotvalidErrorMessage);
        return new ResponseEntity<>(genericErrorModel, HttpStatus.BAD_REQUEST);
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException
     * 
     * @param exception the exception
     * @param headers   the headers to be written to the response
     * @param status    the selected response status
     * @param request   the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception,
                                    HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	IssueDataModel genericErrorModel= new IssueDataModel();
    	genericErrorModel.setReason("Media Type Not Acceptable");
        logErrorMessage(exception);
        return new ResponseEntity<>(genericErrorModel, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException.
     * <p>
     * This method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal}.
     * 
     * @param exception the exception
     * @param headers   the headers to be written to the response
     * @param status    the selected response status
     * @param request   the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
                                    HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	IssueDataModel genericErrorModel= new IssueDataModel();
    	genericErrorModel.setReason("Media Type Not Supported");
        logErrorMessage(exception);
        return new ResponseEntity<>(genericErrorModel, status);
    }

    @Override
    /**
     * Customize the response for NoHandlerFoundException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     * 
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException,
                                    HttpHeaders headers,
                                    HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>("No Handler Found",
                                        HttpStatus.NOT_FOUND);
    }

    @Override
    /**
     * Customize the response for HttpMethodNotSupportedException
     * 
     * @param httpRequestMethodNotSupportedException - the exception
     * @param headers                                - the headers to be written to
     *                                               the response
     * @param status                                 - the selected response status
     * @param request                                - the current request
     *
     * @return a {@code ResponseEntityInstance}
     */
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
                                    HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException,
                                    HttpHeaders headers,
                                    HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>("Method not allowed",
                                        HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Log error message.
     *
     * @param exception                         the exception
     * @param genericExceptionErrorResponsePair the generic exception error response
     *                                          pair
     */
    private void logErrorMessage(final Exception exception) {
        logger.error("Exception occured" + exception.getMessage());
    }
    
    private void logErrorMessage(final String message) {
        logger.error("Exception occured" + message);
    }

}
