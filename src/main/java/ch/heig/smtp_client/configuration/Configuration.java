package ch.heig.smtp_client.configuration;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Paths;
import java.util.Collections;

/**
 * Load the configuration file
 *
 * @author David Cruchon
 * @author Thierry Otto
 * @version 1
 */
public class Configuration {

    private final String CONFIG_PATH = "config.yml";
    private final ConfigurationProvider provider;

    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
        ConfigurationSource source = new FilesConfigurationSource(() -> Collections.singleton(Paths.get(CONFIG_PATH)));
        Environment environment = new ImmutableEnvironment(".");
        provider = new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .withEnvironment(environment)
                .build();
    }

    public <T> T get(String key, Class<T> c) {
        return provider.getProperty(key, c);
    }
}
