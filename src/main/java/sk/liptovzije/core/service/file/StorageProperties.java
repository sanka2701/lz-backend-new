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

    private static Logger logger = LoggerFactory.getLogger(StorageProperties.class);

//    public StorageProperties() {
//        // todo: seems a bit hacky
//        URL path = this.getClass().getClassLoader().getResource("static/img");
//        String stringPath = path.getPath();
//
//        if(stringPath.startsWith("/")) {
//            stringPath = stringPath.substring(1);
//        }
//
//        logger.debug("Store location for files is: " + stringPath);
//
//        this.location = stringPath;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
