package sk.liptovzije.core.service.article;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.article.ArticleFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultArticleService implements ArticleService{
    
    private Set<Article> articleRepo = new HashSet<>();
    private int FUZZY_SCORE_TRESHOLD = 85;
    
    @Override
    public Optional<Article> create(Article article) {
        articleRepo.add(article);
        return Optional.of(article);
    }

    @Override
    public Optional<Article> update(Article article) {
        this.articleRepo.stream()
                .filter(currentArticle -> currentArticle.getId().equals(article.getId()))
                .forEach(updatedArticle -> updatedArticle = article);

        return Optional.ofNullable(article);
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Optional<Article> getById(long id) {
        return this.articleRepo.stream()
                .filter(article -> article.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Article> getByFilter(ArticleFilter filter) {
        return this.articleRepo.stream()
                .filter(article -> {
                    boolean fitsFilter = true;

                    if (filter.getOwnerId() != null) {
                        fitsFilter = fitsFilter && article.getOwnerId().equals(filter.getOwnerId());
                    }

                    if (filter.getTitle() != null) {
                        String normalizedName  = StringUtils.stripAccents(article.getTitle()).toLowerCase();
                        String normalizedQuery = StringUtils.stripAccents(filter.getTitle()).toLowerCase();
                        fitsFilter = fitsFilter && (normalizedName.contains(normalizedQuery)
                                || FuzzySearch.ratio(normalizedName, normalizedQuery) > FUZZY_SCORE_TRESHOLD);
                    }

                    if(filter.getDateAddedFrom() != null) {
                        fitsFilter = fitsFilter &&
                                (article.getDateAdded().isAfter(filter.getDateAddedFrom()) ||
                                        article.getDateAdded().equals(filter.getDateAddedFrom()));
                    }

                    if(filter.getDateAddedUntil() != null) {
                        fitsFilter = fitsFilter &&
                                (article.getDateAdded().isBefore(filter.getDateAddedUntil()) ||
                                        article.getDateAdded().equals(filter.getDateAddedUntil()));
                    }

                    return fitsFilter;
                })
                .collect(Collectors.toList());
    }
}
