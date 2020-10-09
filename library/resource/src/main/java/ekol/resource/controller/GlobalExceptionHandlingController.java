package ekol.resource.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.exceptions.*;

/**
 * Created by kilimci on 10/06/16.
 */
@ControllerAdvice
public class GlobalExceptionHandlingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlingController.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ExceptionResponse> handleError(HttpClientErrorException e) {
		try {
			return new ResponseEntity<>(MAPPER.readValue(e.getResponseBodyAsString(), ExceptionResponse.class), e.getStatusCode());
		}catch(IOException ex) {
			LOGGER.warn("Error response cannat parse to object: " ,ex);
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), e.getStatusCode());
		}
	}
	
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleError(ResourceNotFoundException e) {
    	return new ResponseEntity<>(new ExceptionResponse(e.getRawMessage(), e.getArgs()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException e) {
    	return new ResponseEntity<>(new ExceptionResponse(e.getRawMessage(), e.getArgs()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<String> handleBadRequest(UnauthorizedAccessException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleBadRequest(ValidationException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getRawMessage(), e.getArgs()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleBadRequest(ConstraintViolationException e) {
    	return new ResponseEntity<>(new ExceptionResponse(e.getConstraintViolations().stream().map(t->t.getPropertyPath() + " " + t.getMessage()).collect(Collectors.joining(", "))), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleBadRequest(BindException e) {
    	return new ResponseEntity<>(new ExceptionResponse(e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "))), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleBadRequest(MethodArgumentNotValidException e) {
    	return new ResponseEntity<>(new ExceptionResponse(e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "))), HttpStatus.BAD_REQUEST);
    }
}
