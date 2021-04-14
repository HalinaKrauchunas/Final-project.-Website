package com.epam.kravchunas.fle.services.user;

import com.epam.kravchunas.fle.entities.user.Role;
import com.epam.kravchunas.fle.repositories.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public List<Role> getAllRoles() {
        return StreamSupport.stream(roleRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Transactional
    public Role findRoleById(Long roleId) {
       return roleRepository.findById(roleId).get();
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }
}
