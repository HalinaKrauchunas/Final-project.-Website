package com.epam.kravchunas.fle.entities.subject;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "terms")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Поле не заполнено")
    private String name;

    @NotBlank(message = "Поле не заполнено")
    private String definition;

    @NotBlank(message = "Поле не заполнено")
    @Size(min = 11, message = "Информация об источнике должна превышать 10 символов")
    private String source;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "section_id")
    private Section section;

    public Term() {}

    public Term(String name, String definition, String source) {

        this.name = name;
        this.definition = definition;
        this.source = source;
    }

    public Section getSection() {
        if (section == null){
            section = new Section();
        }
        return section;
    }


    @Override
    public String toString() {

        return "Term{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", definition='" + definition + '\'' +
            ", source='" + source + '\'' +
            '}';
    }
}
