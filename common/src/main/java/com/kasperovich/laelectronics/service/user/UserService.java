package com.kasperovich.laelectronics.service.user;

import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.models.User;

import java.util.List;

public interface UserService {

    User createUser(User user) throws BadPasswordException;

    List<User> findAll();

    User updateUser(User user);

    User deleteUser(Long id);

    User findUserByEmail(String email);

    User findUserByLogin(String login);

}
