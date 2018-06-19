package sk.liptovzije;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.net.URL;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String urlStr = this.getClass().getClassLoader().getResource("static/img").toString() + "/";
        registry.addResourceHandler("/img/**").addResourceLocations(urlStr);
        super.addResourceHandlers(registry);
    }
}