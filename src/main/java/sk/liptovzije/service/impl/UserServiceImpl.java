package sk.liptovzije.service.impl;

//import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import sk.liptovzije.model.DO.QUserDO;
import sk.liptovzije.model.DO.QUserDO;
import sk.liptovzije.model.DO.UserDO;
//import sk.liptovzije.model.RegistrationResponse;
        import sk.liptovzije.service.IUserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	private SessionFactory sessionFactory;

	private List<UserDO> storedUsers = new ArrayList<>();
	private AtomicInteger atomicInteger = new AtomicInteger();

	@Override
	public UserDO getById(long id) {
		HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

		QUserDO user = QUserDO.userDO;
        UserDO result = query.from(user)
                .where(user.id.eq(id))
                .uniqueResult(user);

		return result;
//		UserDO result = null;
//
//		for(UserDO user: this.storedUsers) {
//			if(user.getId() == id){
//				result = user;
//				break;
//			}
//		}
//
//		return result;
	}

	@Override
	public Long saveUser(UserDO user) {
		try {
			sessionFactory.getCurrentSession().save(user);
		}  catch (Exception e) {
			e.printStackTrace();
		}

		return user.getId();
//		user.setId(atomicInteger.getAndIncrement());
//		this.storedUsers.add(user);
	}

	@Override
	public void updateUser(UserDO user) {
        QUserDO userDO = QUserDO.userDO;
        new HibernateUpdateClause(sessionFactory.getCurrentSession(), userDO).where(userDO.id.eq(user.getId()))
                .set(userDO.firstName, user.getFirstName())
                .set(userDO.lastName, user.getLastName())
                .set(userDO.email, user.getEmail())
                .set(userDO.role, user.getRole())
                .execute();
	}

	public void deleteUserById(long id) {
        QUserDO userDO = QUserDO.userDO;
        new HibernateDeleteClause(sessionFactory.getCurrentSession(), userDO).where(userDO.id.eq(id)).execute();

//		for (Iterator<UserDO> iter = this.storedUsers.listIterator(); iter.hasNext(); ) {
//			UserDO user = iter.next();
//			if (user.getId() == id) {
//				iter.remove();
//				break;
//			}
//		}
	}

	public List<UserDO> getAllUsers() {
	    // todo: implement filter
		return null;
//		return this.storedUsers;
	}
}
