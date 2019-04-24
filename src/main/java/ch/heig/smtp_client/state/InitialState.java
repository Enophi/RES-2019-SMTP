package ch.heig.smtp_client.state;

import ch.heig.smtp_client.CRLFTerminatedReader;
import ch.heig.smtp_client.SMTPClient;
import ch.heig.smtp_client.exception.IncorrectCommandException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class InitialState extends SMTPState {

    private static final String ERROR_MESSAGE = "You must start with the EHLO command. Use the method setDomain(String domain) first.";

    public InitialState(SMTPClient client, String smtpServer, Integer smtpPort) {
        super(client);

        // Init the connection with the smtp server
        try {
            _socket = new Socket(smtpServer, smtpPort);
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new CRLFTerminatedReader(_socket.getInputStream());
            System.out.println(_in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDomain(String domain) throws IOException {
        System.out.println(String.format("[SENDING] EHLO %s", domain));

        // Send command to the server
        _out.println(String.format("EHLO %s", domain));

        // TODO Check the response with more efficiency
        _lastStatus = extractResponseCode(_in.readLine());
        _in.readLine();
        _in.readLine();

        // Check if can pass to next State
        if (_lastStatus.equals(RFC_OK))
            _client.setState(new SecondState(this));
    }

    @Override
    public void setSender(String sender) {
        throw new IncorrectCommandException(ERROR_MESSAGE);
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
