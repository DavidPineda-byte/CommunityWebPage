package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserByUsername(String username);
    User getUserById(Integer id);
    void registerUser(User user);
    void deleteUser(User user);
}
