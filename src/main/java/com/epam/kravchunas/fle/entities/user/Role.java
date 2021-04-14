package com.epam.kravchunas.fle.entities.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Поле не заполнено")
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return  name;
    }
}
