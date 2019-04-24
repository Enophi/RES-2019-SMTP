package ch.heig.smtp_client.state;

import ch.heig.smtp_client.CRLFTerminatedReader;
import ch.heig.smtp_client.SMTPClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SMTPState {

    static final Integer RFC_OK = 250;
    static final Integer RFC_START_MAIL = 354;
    static final Integer RFC_SYNTAX_ERROR = 500;
    
    Integer _lastStatus;

    Socket _socket;
    PrintWriter _out;
    CRLFTerminatedReader _in;

    SMTPClient _client;

    SMTPState(SMTPClient client) {
        _client = client;
    }

    SMTPState(SMTPState oldState) {
        _client = oldState._client;
        _out = oldState._out;
        _in = oldState._in;
    }

    public abstract void setDomain(String domain) throws IOException;

    public abstract void setSender(String sender) throws IOException;

    public abstract void setRecipient(String recipient);

    public abstract void setData(String subject, String data);

    public abstract void send();

    Integer extractResponseCode(String response) {
        return Integer.valueOf(response.substring(0, 3));
    }
}
