package com.epam.kravchunas.fle.services.subject;

import com.epam.kravchunas.fle.entities.subject.Term;
import com.epam.kravchunas.fle.repositories.subject.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TermService {

    private TermRepository termRepository;
    @Autowired
    public void setTermRepository(TermRepository termRepository) {
        this.termRepository = termRepository;
    }


    @Transactional
    public List<Term> findByName(String nameTerm){
        return termRepository.findByName(nameTerm);
    }

    @Transactional
    public Term getTermById(long id){
        return termRepository.findById(id).get();
    }

    @Transactional
    public void saveTerm(Term term) {
        termRepository.save(term);
    }

    @Transactional
    public Page<Term> findAllTerms(Pageable pageable){
        return termRepository.findAll(pageable);
    }

    @Transactional
    public Page<Term> findAllTermsBySectionId(int sectionId, PageRequest of) {
        return termRepository.findAllBySectionId(sectionId, of);
    }

    @Transactional
    public Page<Term> findAllByNameStartsWith(String word, PageRequest pageRequest){
        return  termRepository.findAllByNameStartsWith(word, pageRequest);
    }

    @Transactional
    public Page<Term> findAllByNameStartsWithAndSectionId(String word, int id, Pageable pageable){
        return termRepository.findAllByNameStartsWithAndSectionId(word, id, pageable);
    }

    @Transactional
    public void deleteTermById(long id) {
        termRepository.deleteById(id);
    }
}
