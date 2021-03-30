package pro.inmost.vacancydiary.security.exception;

public class MissingJwtTokenException extends RuntimeException {

    public MissingJwtTokenException(String message) {
        super(message);
    }
}
