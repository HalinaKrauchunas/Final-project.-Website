package com.epam.kravchunas.fle.repositories.user;

import com.epam.kravchunas.fle.entities.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
