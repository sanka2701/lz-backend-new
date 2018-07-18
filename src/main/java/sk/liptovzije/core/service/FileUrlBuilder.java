package sk.liptovzije.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUrlBuilder {
    String toServerUrl(String filePath);
    String replaceUrls(String content, Map<String,String> urlMap);
    Map<String, String> buildFileUrlMap (String[] contentFileUrls, MultipartFile[] files);
}
