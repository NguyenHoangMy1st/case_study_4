package com.kin1st.coursemanagement.service.imp;

import com.kin1st.coursemanagement.model.Role;
import com.kin1st.coursemanagement.repository.IRoleRepository;
import com.kin1st.coursemanagement.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public List<Role> findAll() {
        return iRoleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepository.findById(id);
    }

    @Override
    public Role findByName(String name) {
        return iRoleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return iRoleRepository.save(role);
    }

    @Override
    public void remove(Long id) {
        iRoleRepository.deleteById(id);
    }

}