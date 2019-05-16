package sk.liptovzije.core.service.article;

import sk.liptovzije.application.article.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Optional<Article> create (Article article);

    void update (Article article);

    void delete (long id);

    Optional<Article> getById(long id);

    List<Article> getAll();
}
