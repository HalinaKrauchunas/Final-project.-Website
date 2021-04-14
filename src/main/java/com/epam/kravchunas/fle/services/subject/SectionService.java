package com.epam.kravchunas.fle.services.subject;

import com.epam.kravchunas.fle.entities.subject.Section;
import com.epam.kravchunas.fle.repositories.subject.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SectionService {

    private SectionRepository sectionRepository;

    @Autowired
    public void setSectionRepository(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public List<Section> getAllSection(){
        return StreamSupport.stream(sectionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(Section section) {
        sectionRepository.save(section);
    }

    @Transactional
    public Section getSectionById(int sectionId) {
    return sectionRepository.findById(sectionId).get();
    }

    @Transactional
    public void deleteSection(int sectionId) {
        sectionRepository.deleteById(sectionId);
    }
}
