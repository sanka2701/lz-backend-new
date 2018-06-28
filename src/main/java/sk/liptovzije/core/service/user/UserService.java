package sk.liptovzije.core.service.user;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.user.QUser;
import sk.liptovzije.application.user.Roles;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.encrypt.IEncryptService;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService implements IUserService {
//    @Autowired
//    private SessionFactory sessionFactory;

    List<User> usersRepo = new ArrayList<>();


    @Autowired
    public UserService(){
        User admin = new User("admin@lz.sk", "admin", "admin");
        admin.setRole(Roles.ROLE_ADMIN);

        usersRepo.add(admin);
    }

    @Override
    public Optional<User> save(User user) {
//        try {
//            sessionFactory.getCurrentSession().create(user);
//        }  catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return Optional.ofNullable(user);
        usersRepo.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        usersRepo.stream()
                .filter(currentUser -> currentUser.getId().equals(user.getId()))
                .forEach(updatedUser -> updatedUser = user);

        return Optional.empty();
    }

    @Override
    public List<User> filter() {
        return usersRepo;
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
       return usersRepo.stream()
               .filter(user -> user.getId().equals(id))
               .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return usersRepo.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepo.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
