package com.epam.kravchunas.fle.services.user;

import com.epam.kravchunas.fle.entities.subject.Term;
import com.epam.kravchunas.fle.entities.user.MyUserPrincipal;
import com.epam.kravchunas.fle.entities.user.User;
import com.epam.kravchunas.fle.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "unable to find user by username: " + username));
    }

    @Transactional
    public boolean isPresentUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new MyUserPrincipal(user);
    }

    @Transactional
    public void saveUser(User user)  {
        userRepository.save(user);
    }

    @Transactional
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public Page<User> findAllUser(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    @Transactional
    public User findUserById(Long userId) {
       return userRepository.findById(userId).get();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
