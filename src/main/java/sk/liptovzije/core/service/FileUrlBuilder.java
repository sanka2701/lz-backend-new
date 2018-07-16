package sk.liptovzije.core.service;

import java.util.Map;

public interface FileUrlBuilder {
    String toServerUrl(String filePath);
    String replaceUrls(String content, Map<String,String> urlMap);
}
