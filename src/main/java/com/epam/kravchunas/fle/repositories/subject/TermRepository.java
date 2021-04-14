package com.epam.kravchunas.fle.repositories.subject;

import com.epam.kravchunas.fle.entities.subject.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TermRepository extends CrudRepository<Term, Long> {

    List<Term> findByName(String nameTerm);
    Page<Term> findAll(Pageable pageable);
    Page<Term> findAllBySectionId(int sectionId, Pageable pageable);

    Page<Term> findAllByNameStartsWith(String word, Pageable pageable);
    Page<Term> findAllByNameStartsWithAndSectionId(String word, int id, Pageable pageable);


}
