package sk.liptovzije.core.service.article;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.article.Article;
import sk.liptovzije.application.article.QArticle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//todo: remove fuzzy search from maven dependencies

@Service
public class DefaultArticleService implements ArticleService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Article> create(Article article) {
        entityManager.unwrap(Session.class).save(article);
        return Optional.of(article);
    }

    @Override
    @Transactional
    public void update(Article article) {
        entityManager.unwrap(Session.class).update(article);
    }

    @Override
    @Transactional
    public void delete(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QArticle article = QArticle.article;
        query.delete(article).where(article.id.eq(id)).execute();
    }

    @Override
    @Transactional
    public Optional<Article> getById(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QArticle article = QArticle.article;
        Article result = query.selectFrom(article)
                .where(article.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    @Transactional
    public List<Article> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QArticle article = QArticle.article;
        return query.selectFrom(article).fetch();
    }
}
