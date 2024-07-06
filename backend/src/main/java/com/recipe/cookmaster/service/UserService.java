package com.recipe.cookmaster.service;

import com.recipe.cookmaster.entity.User;

import java.util.List;

public interface UserService {
    String addUser(User user);

    String editUser(User user);

    List<User> getUsers();

    User getUser(long userId);

    long getUserId(String name);

    String deleteUser(long id);

    String verifyUser(String name, String password);

}
