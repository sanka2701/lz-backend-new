package sk.liptovzije.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.file.File;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class DefaultFileUrlBuilder implements IFileUrlBuilder {

    private String serverUrlPlaceholder;

    @Autowired
    public DefaultFileUrlBuilder(@Value("${app.serverUrlPlaceholder}") String urlPlaceholder) {
        this.serverUrlPlaceholder = urlPlaceholder;
    }

    @Override
    public Map<String, File> buildFileUrlMap (List<String> contentFileUrls, List<File> files) {
        if(contentFileUrls != null && files != null && contentFileUrls.size() != files.size()) {
            throw new IllegalArgumentException("List of files and corresponding urls has to be equal size");
        }

        Map<String, File> urlMap = new HashMap<>();
        for(int i = 0; contentFileUrls != null && files != null && i < files.size(); i++) {
            urlMap.put(contentFileUrls.get(i), files.get(i));
        }
        return urlMap;
    }

    @Override
    public String toServerUrl(Path filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverUrlPlaceholder);
        sb.append("/img/");
        sb.append(filePath.toString().replace("\\", "/"));
        return sb.toString();
    }

    @Override
    public String replaceUrls(String content, Map<String, File> urlMap) {
        return  urlMap.entrySet()
                .stream()
                .map(entry -> (Function<String,String>) url -> {
                    String replacement = toServerUrl(entry.getValue().getPath());
                    return url.replace(entry.getKey(), replacement);
                })
                .reduce(Function.identity(), Function::andThen)
                .apply(content);
    }
}
