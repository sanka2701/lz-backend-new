package sk.liptovzije.core.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class StorageProperties {
//    @Value("${app.storage.location}")
    private String location;

    public StorageProperties() {
        // todo: seems a bit hacky
        URL path = this.getClass().getClassLoader().getResource("static/img");
        this.location = path.getPath().substring(1); // removes starting '/'
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
