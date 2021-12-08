package org.vul.service;

import org.springframework.stereotype.Component;
import org.vul.bean.User;

@Component
public class UserService implements UserServiceImpl{
    public User getUserByUsername(String username) {
        User user = new User(username, "admin");
        return user;
    }
}