package pl.visa.dndCM.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public ResourceNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Long id) {
        super(String.format("Resource with %d not found", id));
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
