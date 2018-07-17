package sk.liptovzije.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class DefaultFileUrlBuilder implements FileUrlBuilder{

    private String serverUrl;

    @Autowired
    public DefaultFileUrlBuilder(@Value("${app.serverUrl}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String toServerUrl(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverUrl);
        sb.append("/img/");
        sb.append(filePath.replace("\\", "/"));
        return sb.toString();
    }

    @Override
    public String replaceUrls(String content, Map<String, String> urlMap) {
        String replaced = urlMap.entrySet()
                .stream()
                .map(entry -> (Function<String,String>) s->s.replace(entry.getKey(), entry.getValue()))
                .reduce(Function.identity(), Function::andThen)
                .apply(content);
        return replaced;
    }
}
