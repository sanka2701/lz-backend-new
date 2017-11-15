package sk.liptovzije.service.impl;

import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.liptovzije.model.DO.QUserCredentialsDO;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.service.ICredentialService;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@Transactional
public class CredentialService implements ICredentialService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentialsDO credentials = this.getByUsername(username);
        if(credentials == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(credentials.getUsername(), credentials.getPassword(), Collections.emptyList());
    }

    @Override
    public Long save(UserCredentialsDO credentials) {
        try {
            sessionFactory.getCurrentSession().save(credentials);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return credentials.getId();
    }

    @Override
    public void update(UserCredentialsDO credentials) {
        QUserCredentialsDO credDo = QUserCredentialsDO.userCredentialsDO;
        new HibernateUpdateClause(sessionFactory.getCurrentSession(), credDo).where(credDo.id.eq(credentials.getId()))
                .set(credDo.password, credentials.getPassword())
                .execute();
    }

    @Override
    public UserCredentialsDO getByUserId(Long id) {
        HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

        QUserCredentialsDO credDo = QUserCredentialsDO.userCredentialsDO;
        UserCredentialsDO result = query.from(credDo)
                .where(credDo.userId.eq(id))
                .uniqueResult(credDo);

        return result;
    }

    @Override
    public UserCredentialsDO getByUsername(String username) {
        HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

        QUserCredentialsDO credDo = QUserCredentialsDO.userCredentialsDO;
        UserCredentialsDO result = query.from(credDo)
                .where(credDo.username.eq(username))
                .uniqueResult(credDo);

        return result;
    }

    @Override
    public void delete(Long userId) {
        QUserCredentialsDO credDo = QUserCredentialsDO.userCredentialsDO;
        new HibernateDeleteClause(sessionFactory.getCurrentSession(), credDo).where(credDo.userId.eq(userId)).execute();
    }
}
