package sk.liptovzije.core.service.user;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService implements IUserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> save(User user) {
        try {
            entityManager.unwrap(Session.class).save(user);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> update(User updatedUser) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        query.update(user)
                .where(user.id.eq(updatedUser.getId()))
                .set(user.role, updatedUser.getRole())
                .execute();

        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        return query.selectFrom(user).fetch();
    }

    @Override
    public Optional<User> findById(Long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        User result = query.selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        User result = query.selectFrom(user)
                .where(user.username.eq(username))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        User result = query.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
