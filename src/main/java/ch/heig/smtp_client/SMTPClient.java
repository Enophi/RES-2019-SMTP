package ch.heig.smtp_client;

import ch.heig.smtp_client.state.InitialState;
import ch.heig.smtp_client.state.SMTPState;

import java.io.IOException;

public class SMTPClient {

    private final String _smtpServer;
    private final Integer _smtpPort;
    private SMTPState _state;

    public SMTPClient(String smtpServer, Integer smtpPort) {
        _smtpServer = smtpServer;
        _smtpPort = smtpPort;
    }

    public void setState(SMTPState state) {
        _state = state;
    }

    public void setDomain(String domain) throws IOException {
        _state.setDomain(domain);
    }

    public void setSender(String sender) throws IOException {
        _state.setSender(sender);
    }

    public void setRecipient(String recipient) {
        _state.setRecipient(recipient);
    }

    public void setData(String subject, String data) {
        _state.setData(subject, data);
    }

    public void send() {
        _state.send();
    }

    public void init() {
        this._state = new InitialState(this, _smtpServer, _smtpPort);
    }
}
