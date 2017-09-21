package sk.liptovzije.service.impl;

//import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import sk.liptovzije.model.DO.QUserDO;
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

//	@Autowired
//	private SessionFactory sessionFactory;

	@Autowired
	private AuthenticatorImpl authService;

	private List<UserDO> storedUsers = new ArrayList<>();
	private AtomicInteger atomicInteger = new AtomicInteger();
	@Override
	public UserDO getByUsername(String username) {
//		HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());
//
//		QUserDO user = QUserDO.userDO;
//		UserDO result = query.from(user)
//				.where(user.username.eq(username))
//				.uniqueResult(user);
//
//		return result;

		UserDO result = null;

		for(UserDO user: this.storedUsers) {
			if(user.getUsername().equals(username)){
				result = user;
				break;
			}
		}

		return result;
	}

	@Override
	public UserDO getById(long id) {
//		HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());
//
//		QUserDO user = QUserDO.userDO;
//        UserDO result = query.from(user)
//                .where(user.id.eq(id))
//                .uniqueResult(user);
//
//		return result;
		UserDO result = null;

		for(UserDO user: this.storedUsers) {
			if(user.getId() == id){
				result = user;
				break;
			}
		}

		return result;
	}

	@Override
	public void saveUser(UserDO user) {
//		try {
//			sessionFactory.getCurrentSession().save(user);
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}

		user.setId(atomicInteger.getAndIncrement());
		this.storedUsers.add(user);
	}

	@Override
	public void updateUser(UserDO user) {
//		try {
//			QUserDO userDO = QUserDO.userDO;
//			new HibernateUpdateClause(getSessionFactory().getCurrentSession(), userDO)
//					.where(userDO.id.eq(user.getId()))
//					.set(userDO.firstName, user.getFirstName())
//					.set(userDO.lastName, user.getLastName())
//					.set(userDO.address, user.getAddress())
//					.set(userDO.email, user.getEmail())
////					username sa asi menit nebude
////					.set(userDO.userName, user.getCredentials().getUsername())
//					.set(userDO.password, user.getCredentials().getPassword())
//					.set(userDO.salt, user.getCredentials().getSalt())
//					.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void deleteUserById(long id) {
		for (Iterator<UserDO> iter = this.storedUsers.listIterator(); iter.hasNext(); ) {
			UserDO user = iter.next();
			if (user.getId() == id) {
				iter.remove();
				break;
			}
		}
	}

	public List<UserDO> getAllUsers() {
//		return null;
		return this.storedUsers;
	}
}
