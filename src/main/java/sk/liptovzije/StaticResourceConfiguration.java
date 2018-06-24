package sk.liptovzije;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sk.liptovzije.core.service.file.StorageProperties;

import java.io.File;
import java.net.URL;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Value("${app.storage.location}")
    private String location;

    private static Logger logger = LoggerFactory.getLogger(StorageProperties.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String urlStr = this.getClass().getClassLoader().getResource("").toString() + "/";

        String urlStr = new File(".").getAbsolutePath();
        urlStr = urlStr.replace("\\", "/");
        urlStr = urlStr.startsWith("/") ? urlStr.substring(1) : urlStr;
        urlStr = urlStr.substring(0,urlStr.length() - 1);
        urlStr = "file:///" + urlStr;

        logger.debug("Path to jar: " + urlStr);

        if(location.startsWith("./")) {
            urlStr += location.substring(2);
        } else if (location.startsWith("/")) {
            urlStr += location.substring(1);
        } else {
            urlStr += location;
        }

        urlStr += urlStr.endsWith("/") ? "" : "/";

        logger.debug("Path to served content: " + urlStr);

        registry.addResourceHandler("/img/**").addResourceLocations(urlStr);
        super.addResourceHandlers(registry);
    }
}