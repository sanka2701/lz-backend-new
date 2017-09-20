package sk.liptovzije.service.impl;

import com.mysema.query.jpa.hibernate.HibernateQuery;
import org.hibernate.SessionFactory;
//import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import sk.liptovzije.model.DO.QUserDO;
import sk.liptovzije.model.DO.QUserDO;
import sk.liptovzije.model.DO.UserDO;
//import sk.liptovzije.model.RegistrationResponse;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.Response;
import sk.liptovzije.model.DO.UserCredentials;
import sk.liptovzije.service.IUserService;

import javax.transaction.Transactional;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl /*extends HibernateDaoSupport*/ implements IUserService {

//	public UserDO getById(long id) {
//		HibernateQuery query = new HibernateQuery(getSessionFactory().getCurrentSession());
//
//		QUserDO user = QUserDO.userDO;
//        UserDO result = query.from(user)
//                .where(user.id.eq(id))
//                .uniqueResult(user);
//
//		return result;
//	}
//
//	public RegistrationResponse isUsernameInUse(UserDO newUser){
//		HibernateQuery query = new HibernateQuery(getSessionFactory().getCurrentSession());
//		QUserDO userDO = QUserDO.userDO;
//
//		List<Tuple> rows = query.from(userDO)
//				.where(userDO.username.eq(newUser.getCredentials().getUsername())
//						.or(userDO.email.eq(newUser.getEmail())))
//				.list(new QTuple(userDO.username, userDO.email));
//
//		RegistrationResponse result = new RegistrationResponse(RegistrationResponse.RESPONSE_CODE.OKAY);
//
//		for(int i=0; i< rows.size(); i++){
//			Tuple response = rows.get(i);
//			if(response.get(userDO.username).equals(newUser.getCredentials().getUsername())) {
//				result.setResult(RegistrationResponse.RESPONSE_CODE.USERNAME_ALRADY_EXISTS);
//				break;
//			}
//			if(response.get(userDO.email).equals(newUser.getEmail())) {
//				result.setResult(RegistrationResponse.RESPONSE_CODE.EMAIL_ALRADY_EXISTS);
//				break;
//			}
//		}
//
//		return result;
//	}
//
//	public void saveUser(UserDO user) {
//		if (!user.hasMandatoryFields()) {
//			throw new IllegalArgumentException("Some mandatory fields are missing");
//		}
//
//		try {
//			user.setCreatedDate(new LocalDate());
//			getHibernateTemplate().save(user);
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	public void updateUser(UserDO user) {
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
//	}
//
//	public UserDO authenticate(final UserCredentials credentials) {
//		if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
//			throw new IllegalArgumentException("Passed credentials are null");
//		}
//
//		HibernateQuery query = new HibernateQuery(getSessionFactory().getCurrentSession());
//		QUserDO userDO = QUserDO.userDO;
//
//		List<Tuple> rows = query.from(userDO)
//				.where(userDO.username.eq(credentials.getUsername()))
//				.list(new QTuple(userDO.id, userDO.username, userDO.password, userDO.salt));
//
//		if(rows.isEmpty()) {
//			// TODO: nespravne prihlasovacie meno
//			return null;
//		}
//
//		Tuple response = rows.get(0);
//		UserCredentials storedCredentials = new UserCredentials(response.get(userDO.username), response.get(userDO.password));
//
//		credentials.setSalt(response.get(userDO.salt));
//		credentials.hashPassword();
//
//		if (!storedCredentials.equals(credentials)) {
//			// TODO: nespravne heslo
//			return null;
//		}
//
//		return getById(response.get(userDO.id));
//	}

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AuthenticatorImpl authService;

	@Override
	public UserDO getByUsername(String username) {
		HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

		QUserDO user = QUserDO.userDO;
		UserDO result = query.from(user)
				.where(user.username.eq(username))
				.uniqueResult(user);

		return result;
	}

	@Override
	public UserDO getById(long id) {
		HibernateQuery query = new HibernateQuery(sessionFactory.getCurrentSession());

		QUserDO user = QUserDO.userDO;
        UserDO result = query.from(user)
                .where(user.id.eq(id))
                .uniqueResult(user);

		return result;
	}

	@Override
	public void saveUser(UserDO user) {
		try {
			sessionFactory.getCurrentSession().save(user);
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(UserDO user) {

	}

	public void deleteUserById(long id) {

	}

	public List<UserDO> getAllUsers() {
		return null;
	}
}
