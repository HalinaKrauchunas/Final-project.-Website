package com.epam.kravchunas.fle.entities.subject;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Поле не заполнено")
    private String name;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "section")
    private List<Term> terms;

    public Section() {

    }

    public Section(String name) {

        this.name = name;
    }

    public List<Term> getTerms() {
        if (terms == null){
            terms = new ArrayList<>();
        }
        return terms;
    }

    @Override
    public String toString() {

        return "Section{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
