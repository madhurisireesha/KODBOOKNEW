
package com.kodbook.services;
import java.util.List;

import com.kodbook.entities.Post;
import com.kodbook.entities.User;
public interface UserService {

	void addUser(User user);

	boolean userExists(String username, String email);

	boolean validateUser(String username, String password);

	User getUser(String username);

	void updateUser(User user);

	User getUserById(Long id);

	User getSpecificUser(Long id);

	User findByEmail(String email);

	void updatePassword(User user, String newPassword);

	
}
