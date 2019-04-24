package ch.heig.smtp_client.exception;

public class MailboxException extends RuntimeException {
    public MailboxException(String message) {
        super(message);
    }
}
