package com.epam.kravchunas.fle.repositories.user;

import com.epam.kravchunas.fle.entities.subject.Term;
import com.epam.kravchunas.fle.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    User findByActivationCode(String code);
    Page<User> findAll(Pageable pageable);

}
