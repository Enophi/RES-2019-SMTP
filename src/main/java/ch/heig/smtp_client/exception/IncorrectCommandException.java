package ch.heig.smtp_client.exception;

public class IncorrectCommandException extends RuntimeException {
    public IncorrectCommandException(String message) {
        super(message);
    }
}
