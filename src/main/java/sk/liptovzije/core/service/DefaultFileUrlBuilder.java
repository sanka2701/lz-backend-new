package sk.liptovzije.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.core.service.file.IStorageService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class DefaultFileUrlBuilder implements FileUrlBuilder{

    private String serverUrl;
    private IStorageService storageService;

    @Autowired
    public DefaultFileUrlBuilder(@Value("${app.serverUrl}") String serverUrl,
                                 IStorageService storageService) {
        this.serverUrl = serverUrl;
        this.storageService = storageService;
    }

    @Override
    public Map<String, String> buildFileUrlMap (String[] contentFileUrls, MultipartFile[] files) {
        if(contentFileUrls != null && files != null && contentFileUrls.length != files.length) {
            throw new IllegalArgumentException("List of files and corresponding urls has to be equal size");
        }

        Map<String, String> urlMap = new HashMap<>();
        for(int i = 0; contentFileUrls != null && files != null && i < files.length; i++) {
            urlMap.put(contentFileUrls[i], toServerUrl(storageService.store(files[i])));
        }
        return  urlMap;
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
        return  urlMap.entrySet()
                .stream()
                .map(entry -> (Function<String,String>) s->s.replace(entry.getKey(), entry.getValue()))
                .reduce(Function.identity(), Function::andThen)
                .apply(content);
    }
}
