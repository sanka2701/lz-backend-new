package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.article.ArticleFilter;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.article.ArticleService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/articles")
public class ArticleApi {

    ArticleService articleService;

    @Autowired
    public ArticleApi(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity createArticle(@Valid @RequestBody ArticleParam param,
                                        @AuthenticationPrincipal User user) {
        Article article = this.paramToEvent(user.getId(), param);
        return this.articleService.create(article)
                .map(storedArticle -> ResponseEntity.ok(this.articleResponse(storedArticle)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateArticle(@Valid @RequestBody ArticleParam updatedArticle,
                                        @AuthenticationPrincipal User user) {
        Article article = this.paramToEvent(user.getId(), updatedArticle);
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
                .thumbnail(param.getThumbnail())
                .build();
    }

    private Map<String, List> articleListResponse(List<Article> articles){
        List<ArticleParam> params = articles.stream()
                .map(ArticleParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("articles", params);
        }};
    }

    private Map<String, Object> articleResponse(Article article) {
        return new HashMap<String, Object>() {{
            put("article", new ArticleParam(article));
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

    public ArticleParam(Article domainArticle) {
        this.id        = domainArticle.getId();
        this.ownerId   = domainArticle.getOwnerId();
        this.title     = domainArticle.getTitle();
        this.content   = domainArticle.getContent();
        this.thumbnail = domainArticle.getThumbnail();
        this.dateAdded = domainArticle.getDateAdded().toDate().getTime();
    }
}
