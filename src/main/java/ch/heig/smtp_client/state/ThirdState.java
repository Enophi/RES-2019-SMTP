package ch.heig.smtp_client.state;

import ch.heig.smtp_client.exception.InconsistentState;
import ch.heig.smtp_client.exception.IncorrectCommandException;
import ch.heig.smtp_client.exception.MailboxException;

import java.io.IOException;

public class ThirdState extends SMTPState {

    private static final String ERROR_MESSAGE = "You must must use setRecipient(String recipient) first";

    ThirdState(SMTPState oldState) {
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
        System.out.println(String.format("[SENDING] RCPT TO: %s", recipient));

        _out.println(String.format("RCPT TO:%s", recipient));
        try {
            String response = _in.readLine();
            System.out.println(response);
            _lastStatus = extractResponseCode(response);
            if (!_lastStatus.equals(RFC_OK))
                throw new MailboxException(String.format("The mail address %s is invalid", recipient));
        } catch (IOException e) {
            e.printStackTrace();
        }

        _client.setState(new FinalState(this));
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
