package goormton.backend.somgil.domain.driver.exception;

public class NoAvailableDriverException extends RuntimeException {
    public NoAvailableDriverException(String message) {
        super(message);
    }
}
