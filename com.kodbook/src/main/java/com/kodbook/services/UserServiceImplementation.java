
package com.kodbook.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodbook.entities.Post;
import com.kodbook.entities.User;
import com.kodbook.repositories.UserRepository;
@Service
public class UserServiceImplementation implements UserService{
	@Autowired
	UserRepository repo;

	public void addUser(User user) {
		repo.save(user);
	}

	@Override
	public boolean userExists(String username, String email) {
		User user1 = repo.findByUsername(username);
		User user2 = repo.findByEmail(email);
		if(user1 != null || user2 !=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean validateUser(String username, String password) {
		String dbPass = repo.findByUsername(username).getPassword();
		if(password.equals(dbPass)) {
			return true;
		}
		return false;
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		 return repo.findByUsername(username);
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		repo.save(user);
	}

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		User user=repo.findById(id).get();
		return user;
	}

	@Override
	public User getSpecificUser(Long id) {
		return repo.findById(id).get();
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return repo.findByEmail(email);
	}
	@Override
	public void updatePassword(User user, String newPassword) {
		// TODO Auto-generated method stub
		user.setPassword(newPassword);
		repo.save(user);
		
	}


}
