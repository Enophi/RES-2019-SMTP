package ch.heig.smtp_client.exception;

public class InconsistentState extends RuntimeException {
    public InconsistentState(String message) {
        super(message);
    }
}
