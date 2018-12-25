package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.article.ArticleFilter;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.article.ArticleService;
import sk.liptovzije.core.service.storage.IStorageService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/articles")
public class ArticleApi {

    private ArticleService articleService;
    private IStorageService storageService;
    private IFileUrlBuilder pathBuilder;

    @Autowired
    public ArticleApi(ArticleService articleService,
                      IStorageService storageService,
                      IFileUrlBuilder pathBuilder) {
        this.articleService = articleService;
        this.storageService = storageService;
        this.pathBuilder = pathBuilder;
    }

    @PostMapping
    public ResponseEntity createArticle(@RequestParam("article") String eventJson,
                                        @RequestParam("thumbnail") MultipartFile thumbnail,
                                        @RequestParam(value = "fileUrls", required = false) String[] contentFileUrls,
                                        @RequestParam(value = "storage", required = false) MultipartFile[] files,
                                        @AuthenticationPrincipal User user) throws IOException {
        ArticleParam param = ArticleParam.fromJson(eventJson);
        Article article = this.paramToEvent(user.getId(), param);

        //todo
//        Map<String, String> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, files);
//        article.setContent(pathBuilder.replaceUrls(article.getContent(), urlMap));
//        article.setThumbnail(pathBuilder.toServerUrl(storageService.store(thumbnail)));

        return this.articleService.create(article)
                .map(storedArticle -> ResponseEntity.ok(this.articleResponse(storedArticle)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateArticle(@RequestParam("article") String eventJson,
                                        @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                        @RequestParam(value = "fileUrls", required = false) String[] contentFileUrls,
                                        @RequestParam(value = "storage", required = false) MultipartFile[] files,
                                        @AuthenticationPrincipal User user) throws IOException {
        ArticleParam param = ArticleParam.fromJson(eventJson);
        Article article = this.paramToEvent(user.getId(), param);

        //todo
//        Map<String, String> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, files);
//        article.setContent(pathBuilder.replaceUrls(article.getContent(), urlMap));
//        if(thumbnail != null) {
//            article.setThumbnail(pathBuilder.toServerUrl(storageService.store(thumbnail)));
//        }

        return this.articleService.update(article)
                .map(storedEvent -> ResponseEntity.ok(this.articleResponse(article)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(path = "/filter")
    public ResponseEntity filterArticles(@Valid @RequestBody ArticleFilter filter) {
        List<Article> articles = this.articleService.getByFilter(filter);
        return ResponseEntity.ok(articleListResponse(articles));
    }

    @DeleteMapping
    public ResponseEntity deleteArticle(@RequestParam("id") long id) {
        this.articleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getArticle(@RequestParam("id") long id) {
        return this.articleService.getById(id)
                .map(article -> ResponseEntity.ok(this.articleResponse(article)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    private Article paramToEvent(Long userId, ArticleParam param) {
        return new Article.Builder(userId, param.getTitle(), param.getContent())
                .id(param.getId())
                .thumbnail(param.getThumbnail())
                .build();
    }

    private Map<String, List> articleResponse(Article article) {
        return articleListResponse(Stream.of(article).collect(Collectors.toList()));
    }

    private Map<String, List> articleListResponse(List<Article> articles){
        List<ArticleParam> params = articles.stream()
                .map(ArticleParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("articles", params);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class ArticleParam {
    private Long id;
    private Long ownerId;
    private String title;
    private String content;
    private String thumbnail;
    private Long dateAdded;

    public static ArticleParam fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(json, typeFactory.constructType(ArticleParam.class));
    }

    public ArticleParam(Article domainArticle) {
        this.id        = domainArticle.getId();
        this.ownerId   = domainArticle.getOwnerId();
        this.title     = domainArticle.getTitle();
        this.content   = domainArticle.getContent();
        this.thumbnail = domainArticle.getThumbnail();
        this.dateAdded = domainArticle.getDateAdded().toDate().getTime();
    }
}
