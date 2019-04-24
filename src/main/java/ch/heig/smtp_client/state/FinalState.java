package ch.heig.smtp_client.state;

import ch.heig.smtp_client.exception.InconsistentState;
import ch.heig.smtp_client.exception.IncorrectCommandException;

import java.io.IOException;

public class FinalState extends SMTPState {

    FinalState(SMTPState oldState) {
        super(oldState);
    }

    @Override
    public void setDomain(String domain) {
        throw new InconsistentState("You shouldn't be here. Run, you fools!");
    }

    @Override
    public void setSender(String sender) {
        throw new InconsistentState("You shouldn't be here. Run, you fools!");
    }

    @Override
    public void setRecipient(String recipient) {
        throw new InconsistentState("You shouldn't be here. Run, you fools!");
    }

    @Override
    public void setData(String subject, String data) {
        System.out.println("[SENDING] DATA");
        _out.println("DATA");
        try {
            String response = _in.readLine();
            System.out.println(response);
            _lastStatus = extractResponseCode(response);
            if (_lastStatus.equals(SMTPState.RFC_START_MAIL)) {
                _out.println(String.format("subject:%s\n", subject));
                _out.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send() {
        if (!_lastStatus.equals(RFC_START_MAIL))
            throw new IncorrectCommandException("You have to set some data before send. Use setData(String subject, String data) first");

        _out.println("\r\n.\r\n");
        try {
            _lastStatus = extractResponseCode(_in.readLine());
            if (_lastStatus.equals(RFC_OK)) {
                _out.println("QUIT");
                _out.close();
                _in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
