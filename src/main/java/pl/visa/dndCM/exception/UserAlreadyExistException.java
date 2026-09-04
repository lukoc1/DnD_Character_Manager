package pl.visa.dndCM.exception;

public class UserAlreadyExistException extends RuntimeException {
    private ErrorCode errorCode = ErrorCode.USER_ALREADY_EXIST;

//    public UserAlreadyExistException(String message) {
//        super(message);
//    }

    public UserAlreadyExistException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserAlreadyExistException(String userName) {
        super(String.format("Username '%s' already exist.", userName));
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
