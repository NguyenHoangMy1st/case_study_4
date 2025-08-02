package com.kin1st.coursemanagement.service;


import com.kin1st.coursemanagement.model.User;

import java.util.Optional;

public interface IUserService extends IGenerateService<User> {
    Optional<User> findByUsername(String username);
}