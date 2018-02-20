package sk.liptovzije.core.service.user;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service
public class UserService implements IUserService {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<User> save(User user) {
        try {
            sessionFactory.getCurrentSession().save(user);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> update(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return findById(Long.parseLong(id));
    }

    @Override
    public Optional<User> findById(Long id) {
        HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

        QUser user = QUser.user;
        User result = query.from(user)
                .where(user.id.eq(id))
                .uniqueResult(user);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return null;
    }
}
