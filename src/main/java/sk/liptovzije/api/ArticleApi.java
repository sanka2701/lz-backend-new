package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.article.ArticleService;
import sk.liptovzije.core.service.post.IPostService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/articles")
public class ArticleApi {

    private ArticleService articleService;
    private IPostService postService;
    private IFileUrlBuilder pathBuilder;

    @Autowired
    public ArticleApi(ArticleService articleService,
                      IPostService postService,
                      IFileUrlBuilder pathBuilder) {
        this.articleService = articleService;
        this.postService = postService;
        this.pathBuilder = pathBuilder;
    }

    @PostMapping
    public ResponseEntity createArticle(@RequestParam("article") String eventJson,
                                        @RequestParam("thumbnail") MultipartFile thumbnail,
                                        @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                        @RequestParam(value = "file", required = false) MultipartFile[] files,
                                        @AuthenticationPrincipal User user) throws IOException {
        ArticleParam param = ArticleParam.fromJson(eventJson);
        Article article = this.paramToDomain(user.getId(), param);

        postService.resolveFileDependencies(article, thumbnail, fileUrls, files);

        return this.articleService.create(article)
                .map(storedArticle -> ResponseEntity.ok(this.articleResponse(storedArticle)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateArticle(@RequestParam("article") String eventJson,
                                        @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                        @RequestParam(value = "fileUrls", required = false) String[] contentFileUrls,
                                        @RequestParam(value = "file", required = false) MultipartFile[] files,
                                        @AuthenticationPrincipal User user) throws IOException {
        ArticleParam param = ArticleParam.fromJson(eventJson);
        Article updatedArticle  = this.paramToDomain(user.getId(), param);
        Article originalArticle =
                this.articleService.getById(updatedArticle.getId()).orElseThrow(ResourceNotFoundException::new);

        this.postService.resolveFileDependencies(updatedArticle, thumbnail, contentFileUrls, files);
        Set<File> toBeDeleted =
                this.postService.updateContentFiles(originalArticle, updatedArticle);

        originalArticle.setContent(updatedArticle.getContent());
        originalArticle.setTitle(updatedArticle.getTitle());
        originalArticle.setFiles(updatedArticle.getFiles());

        this.articleService.update(originalArticle);
        this.postService.removeUnusedFiles(toBeDeleted);

        return ResponseEntity.ok(this.articleResponse(originalArticle));
    }

    @GetMapping(path = "/all")
    public ResponseEntity getAllArticles() {
        List<Article> articles = this.articleService.getAll();
        return ResponseEntity.ok(articleListResponse(articles));
    }

    @DeleteMapping
    public ResponseEntity deleteArticle(@RequestParam("id") long id) {
        this.articleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getArticleById(@RequestParam("id") long id) {
        return this.articleService.getById(id)
                .map(article -> ResponseEntity.ok(this.articleResponse(article)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    private ArticleParam domainToParam(Article domainArticle) {
        ArticleParam param = new ArticleParam();

        param.setId        (domainArticle.getId());
        param.setOwnerId   (domainArticle.getOwnerId());
        param.setTitle     (domainArticle.getTitle());
        param.setContent   (domainArticle.getContent());
        param.setThumbnail (pathBuilder.toServerUrl(domainArticle.getThumbnail().getPath()));
        param.setDateAdded (domainArticle.getDateAdded().toDate().getTime());

        return param;
    }

    private Article paramToDomain(Long userId, ArticleParam param) {
        return new Article.Builder(userId, param.getTitle(), param.getContent())
                .id(param.getId())
                .build();
    }

    private Map<String, List> articleResponse(Article article) {
        return articleListResponse(Stream.of(article).collect(Collectors.toList()));
    }

    private Map<String, List> articleListResponse(List<Article> articles){
        List<ArticleParam> params = articles.stream()
                .map(this::domainToParam)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("articles", params);
        }};
    }
}

@Getter
@Setter
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
}
