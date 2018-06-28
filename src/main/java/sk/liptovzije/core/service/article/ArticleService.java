package sk.liptovzije.core.service.article;

import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.article.ArticleFilter;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Optional<Article> create (Article article);

    Optional<Article> update (Article article);

    void delete (long id);

    Optional<Article> getById(long id);

    List<Article> getByFilter(ArticleFilter filter);
}
