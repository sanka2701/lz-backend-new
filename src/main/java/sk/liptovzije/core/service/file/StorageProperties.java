package sk.liptovzije.core.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class StorageProperties {
    @Value("${app.storage.location}")
    private String location;

    public String getLocation() {
        return location;
    }
}
