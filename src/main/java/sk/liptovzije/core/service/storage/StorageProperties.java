package sk.liptovzije.core.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {
    @Value("${app.storage.location}")
    private String location;

    public String getLocation() {
        return location;
    }
}
