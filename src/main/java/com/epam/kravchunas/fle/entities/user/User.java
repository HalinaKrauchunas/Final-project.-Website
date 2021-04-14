package com.epam.kravchunas.fle.entities.user;

import com.epam.kravchunas.fle.validation.CheckEmail;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Поле не заполнено")
    private String username;

    @Size(min = 7, max = 80, message = "Пароль должен содержать не менее 7 символов")
    private String password;

    @NotBlank(message = "Поле не заполнено")
    @CheckEmail
    private String email;

    private boolean active;

    @Column(name = "activation_code")
    private String activationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Collection<Role> getRoles() {
        if (roles == null){
            roles = new HashSet<>();
        }
        return roles;
    }

    public boolean isAdmin(){
        return this.roles.stream().map(Role::getName).anyMatch(name->name.equals("ROLE_ADMIN"));
    }

    public boolean isExpert(){
        return this.roles.stream().map(Role::getName).anyMatch(name->name.equals("ROLE_EXPERT"));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", activationCode='" + activationCode + '\'' +
                ", roles=" + roles +
                '}';
    }
}