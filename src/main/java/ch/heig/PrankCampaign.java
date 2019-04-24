package ch.heig;

import ch.heig.smtp_client.SMTPClient;
import ch.heig.smtp_client.configuration.Configuration;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * The main app class.
 *
 * @author David Cruchon
 * @author Thierry Otto
 * @version 1
 */
public class PrankCampaign implements Runnable {

    private static final String FILES_VICTIM_CONFIG = "files.victim";
    private static final String FOLDER_MESSAGE_CONFIG = "folder.message";
    private static final String SMTP_SERVER_CONFIG = "smtp.server";
    private static final String SMTP_PORT_CONFIG = "smtp.port";
    private static final String SMTP_DOMAIN_CONFIG = "smtp.domain";
    private static final String GROUP_SIZE_CONFIG = "group_size";

    private Path _listOfVictim;
    private List<Path> _messages;

    private SMTPClient _client;

    private PrankCampaign() {

        String victimFileName = Configuration.getInstance().get(FILES_VICTIM_CONFIG, String.class);
        String messageFolderPath = Configuration.getInstance().get(FOLDER_MESSAGE_CONFIG, String.class);

        try {
            _listOfVictim = Paths.get(victimFileName);
            _messages = Files.list(Paths.get(messageFolderPath)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        _client = new SMTPClient(Configuration.getInstance().get(SMTP_SERVER_CONFIG, String.class), Configuration.getInstance().get(SMTP_PORT_CONFIG, Integer.class));
    }

    @Override
    public void run() {
        String domain = Configuration.getInstance().get(SMTP_DOMAIN_CONFIG, String.class);
        Integer groupSize = Configuration.getInstance().get(GROUP_SIZE_CONFIG, Integer.class);

        try {
            Dispatcher.chunk(Files.lines(_listOfVictim).collect(Collectors.toList()), groupSize).forEach(group -> {

                Collections.shuffle(group);
                String sender = group.remove(0);
                Pair<String, String> data = getRandomMessage();
                group.forEach(victim -> {
                    try {
                        _client.init();
                        _client.setDomain(domain);
                        _client.setSender(sender);
                        _client.setRecipient(victim);
                        _client.setData(Objects.requireNonNull(data).getKey(), data.getValue());
                        _client.send();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Pick a random message from the configured FOLDER_MESSAGE_CONFIG path
     *
     * @return The random message
     */
    private Pair<String, String> getRandomMessage() {
        Path p = _messages.get(ThreadLocalRandom.current().nextInt(0, _messages.size()));
        try {
            return new Pair<>(FilenameUtils.removeExtension(p.getFileName().toString()), Files.lines(p).collect(Collectors.joining("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        // Start a new thread for a campain
        new Thread(new PrankCampaign()).start();

    }
}
