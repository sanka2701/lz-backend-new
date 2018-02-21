package sk.liptovzije.core.service.user;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.User;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserService implements IUserService {
//    @Autowired
//    private SessionFactory sessionFactory;

    @Override
    public Optional<User> save(User user) {
//        try {
//            sessionFactory.getCurrentSession().save(user);
//        }  catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return Optional.ofNullable(user);
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
//        HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());
//
//        QUser user = QUser.user;
//        User result = query.from(user)
//                .where(user.id.eq(id))
//                .uniqueResult(user);
//
//        return Optional.ofNullable(result);
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> res = Optional.empty();
        return res;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
