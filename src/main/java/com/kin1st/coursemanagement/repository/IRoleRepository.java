package com.kin1st.coursemanagement.repository;

import com.kin1st.coursemanagement.model.Role;
import com.kin1st.coursemanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}