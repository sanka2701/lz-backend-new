package sk.liptovzije.core.service.user;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.Roles;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.encrypt.IEncryptService;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService implements IUserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(){
//        User admin = new User("admin@lz.sk", "admin", "admin");
//        admin.setRole(Roles.ROLE_ADMIN);
//        this.save(admin);
    }

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
    public Optional<User> update(User user) {
        // todo: not yet implemented
        return Optional.empty();
    }

    @Override
    public List<User> filter() {
        // todo: not yet implemented
        return new ArrayList<>();
    }

    @Override
    public Optional<User> findById(Long id) {
        HibernateQuery query = new HibernateQuery(entityManager.unwrap(Session.class));

        QUser user = QUser.user;
        User result = query.from(user)
                .where(user.id.eq(id))
                .uniqueResult(user);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        HibernateQuery query = new HibernateQuery(entityManager.unwrap(Session.class));
        QUser user = QUser.user;
        User result = query.from(user)
                .where(user.username.eq(username))
                .uniqueResult(user);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // todo: not yet implemented
        return Optional.empty();
    }
}
