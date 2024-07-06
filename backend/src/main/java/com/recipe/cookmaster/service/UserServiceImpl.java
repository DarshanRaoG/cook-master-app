package com.recipe.cookmaster.service;

import com.recipe.cookmaster.entity.User;
import com.recipe.cookmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public String addUser(User user) {

        if(userRepository.findByName(user.getName()) != null)
            return "Error: Username already exists!";

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public String editUser(User user) {
        User existingUser = userRepository.findByName(user.getName());
        if(existingUser != null && existingUser.getId() != user.getId())
            return "Error: Username already exists!";

        userRepository.save(user);
        return "User details edited successfully!";
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(long userId) {
        try {
            User user = userRepository.findById(userId).get();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getUserId(String name) {
        User existingUser = userRepository.findByName(name);
        if(existingUser == null)
            return -1;
        return existingUser.getId();
    }

    @Override
    public String deleteUser(long id) {
        if(!userRepository.existsById(id))
            return "Error: No such user!";

        userRepository.deleteById(id);
        return "User deleted successfully!";
    }

    @Override
    public String verifyUser(String name, String password) {
        User user = userRepository.findByNameAndPassword(name, password);
        if(user != null)
            return "User verified!";
        return "Unverified user!";
    }
}
