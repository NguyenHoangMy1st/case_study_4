package com.kin1st.coursemanagement.service;

import com.kin1st.coursemanagement.model.Role;
import com.kin1st.coursemanagement.model.User;

public interface IRoleService extends IGenerateService<Role>  {
    Role findByName(String name);
}