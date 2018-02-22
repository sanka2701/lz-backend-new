package sk.liptovzije.api.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sk.liptovzije.utils.exception.InvalidRequestException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;

        List<FieldErrorResource> errorResources = ire.getErrors().getFieldErrors().stream().map(fieldError ->
            new FieldErrorResource(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getCode(),
                fieldError.getDefaultMessage())).collect(Collectors.toList());

        ErrorResource error = new ErrorResource(errorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Object> res = handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
        return res;
    }
}
