package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.Response;

import java.util.List;


public interface IUserService {
	UserDO getByUsername(String username);

	UserDO getById(long id);

	void saveUser(UserDO user);
	
	void updateUser(UserDO user);
	
	void deleteUserById(long id);

	List<UserDO> getAllUsers();
}
