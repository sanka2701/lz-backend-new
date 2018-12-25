package sk.liptovzije.core.service;

import sk.liptovzije.application.file.File;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IFileUrlBuilder {
    String toServerUrl(Path filePath);
    String replaceUrls(String content, Map<String,File> urlMap);
    Map<String, File> buildFileUrlMap (List<String> contentFileUrls, List<File> files);
}
