package ch.heig.smtp_client.state;

import ch.heig.smtp_client.exception.InconsistentState;
import ch.heig.smtp_client.exception.IncorrectCommandException;

import java.io.IOException;

public class SecondState extends SMTPState {

    private static final String ERROR_MESSAGE = "You must must use setSender(String sender) first";

    SecondState(SMTPState oldState) {
        super(oldState);
    }

    @Override
    public void setDomain(String domain) {
        throw new InconsistentState("You shouldn't be here. Run, you fools!");
    }

    @Override
    public void setSender(String sender) throws IOException {
        System.out.println(String.format("[SENDING] MAIL FROM:%s", sender));

        _out.println(String.format("MAIL FROM: %s", sender));
        _lastStatus = extractResponseCode(_in.readLine());

        if (_lastStatus.equals(RFC_OK))
            _client.setState(new ThirdState(this));

    }

    @Override
    public void setRecipient(String recipient) {
        throw new IncorrectCommandException(ERROR_MESSAGE);
    }

    @Override
    public void setData(String subject, String data) {
        throw new IncorrectCommandException(ERROR_MESSAGE);
    }

    @Override
    public void send() {
        throw new IncorrectCommandException(ERROR_MESSAGE);
    }
}
