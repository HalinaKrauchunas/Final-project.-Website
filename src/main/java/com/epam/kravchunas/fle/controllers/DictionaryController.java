package com.epam.kravchunas.fle.controllers;


import com.epam.kravchunas.fle.entities.subject.Section;
import com.epam.kravchunas.fle.entities.subject.Term;
import com.epam.kravchunas.fle.entities.user.User;
import com.epam.kravchunas.fle.services.subject.SectionService;
import com.epam.kravchunas.fle.services.subject.TermService;
import com.epam.kravchunas.fle.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {

    List<String> words = List.of("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И",
            "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т",
            "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Э", "Ю", "Я");

    private TermService termService;
    private SectionService sectionService;
    private UserService userService;

    @Autowired
    public void setTermService(TermService termService) {
        this.termService = termService;
    }

    @Autowired
    public void setSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String goToDictionary(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("words", words);
        model.addAttribute("message", "");
        return "dictionaryPages/dictionary";
    }

    @GetMapping("/paginationTerm")
    public String goToPageWithTerms(
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "word", required = false, defaultValue = "") String word,
            @RequestParam(value = "sectionId", required = false, defaultValue = "") String sectionId,
            Model model, Principal principal) {
        Page<Term> termsPage;

        if (word.isEmpty() && sectionId.isEmpty()) {
            termsPage = termService.findAllTerms(PageRequest.of(page, size));
        } else if (word.isEmpty()) {
            termsPage = termService.findAllTermsBySectionId(Integer.parseInt(sectionId), PageRequest.of(page, size));
            model.addAttribute("sectionName", sectionService.getSectionById(Integer.parseInt(sectionId)).getName());
        } else if (sectionId.isEmpty()) {
            termsPage = termService.findAllByNameStartsWith(word, PageRequest.of(page, size));
        } else {
            termsPage = termService.findAllByNameStartsWithAndSectionId(
                    word, Integer.parseInt(sectionId), PageRequest.of(page, size));
        }

        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("words", words);

        model.addAttribute("word", word);
        model.addAttribute("sectionId", sectionId);

        model.addAttribute("termsPage", termsPage);
        model.addAttribute("numbers", IntStream.range(0, termsPage.getTotalPages()).toArray());

        return "dictionaryPages/pagination_term";
    }


    @GetMapping("/search")
    public String goToSearch(@RequestParam("nameTerm") String nameTerm, Model model,
                             RedirectAttributes redirectAttrs) {
        List<Term> terms = termService.findByName(nameTerm);
        System.out.println(terms);
        if (terms.size() == 0) {
            redirectAttrs.addFlashAttribute("message", "Ни одного термина не было найдено");
            return "redirect:/dictionary#term_section";
        } else if (terms.size() == 1) {
            model.addAttribute("term", terms.get(0));
            return "dictionaryPages/term_view";
        } else {
            model.addAttribute("terms", terms);
            return "dictionaryPages/choice_term";
        }
    }

    @GetMapping("/goToTerm")
    public String goToTerm(@RequestParam("termId") long id, Model model) {
        Term term = termService.getTermById(id);
        model.addAttribute("term", term);
        return "dictionaryPages/term_view";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXPERT')")
    @GetMapping("/addNewTerm")
    public String addNewTerm(Model model, Principal principal) {
        Term term = new Term();
        model.addAttribute("term", term);
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("idSection", null);
        model.addAttribute("sections", sectionService.getAllSection());
        return "dictionaryPages/addTerm";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXPERT')")
    @GetMapping("/saveTerm")
    public String saveTerm(@Valid @ModelAttribute("term") Term term,
                           BindingResult bindingResult,
                           @RequestParam("idSection") int id,
                           Model model) {
        if (!bindingResult.hasErrors()) {
            termService.saveTerm(term);
            Section choiceSection = sectionService.getSectionById(id);
            term.setSection(choiceSection);
            choiceSection.getTerms().add(term);
            sectionService.save(choiceSection);
            return "redirect:/dictionary";
        } else {
            model.addAttribute("sections", sectionService.getAllSection());
            return "dictionaryPages/addTerm";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/deleteTerm")
    public String deleteTerm(@RequestParam("termId") int termId,
                             Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        Term term = termService.getTermById(termId);
        term.setSection(null);
        termService.saveTerm(term);
        termService.deleteTermById(termId);
        return "redirect:/dictionary/paginationTerm";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXPERT')")
    @GetMapping("/changeTerm")
    public String changeTerm(@RequestParam("termId") int id, Model model) {
        Term term = termService.getTermById(id);
        model.addAttribute("term", term);
        model.addAttribute("sections", sectionService.getAllSection());
        return "dictionaryPages/addTerm";
    }


//    Sections

    @GetMapping("/goToSections")
    public String goToSections(Model model, Principal principal) {
        Section section = new Section();
        model.addAttribute("section", section);

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("sections", sectionService.getAllSection());
        return "dictionaryPages/sections";
    }

    @PostMapping("/addSection")
    public String saveSection(@Valid @ModelAttribute("section") Section section,
                              BindingResult bindingResult, Model model, Principal principal) {
        if (!bindingResult.hasErrors()) {
            sectionService.save(section);
            return "redirect:/dictionary/goToSections";
        } else {
            model.addAttribute("user", userService.findByUsername(principal.getName()));
            model.addAttribute("sections", sectionService.getAllSection());
            return "dictionaryPages/sections";
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deleteSection")
    public String deleteSection(@RequestParam("sectionId") int sectionId) {
        sectionService.deleteSection(sectionId);
        return "redirect:/dictionary/goToSections";
    }
}