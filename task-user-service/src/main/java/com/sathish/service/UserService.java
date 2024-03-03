package com.sathish.service;

import com.sathish.model.User;

import java.util.List;

public interface UserService {

    public User getUserProfiles(String jwt);

    List<User> getAllUser();
}
