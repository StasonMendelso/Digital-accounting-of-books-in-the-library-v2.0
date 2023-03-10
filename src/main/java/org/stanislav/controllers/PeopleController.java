package org.stanislav.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.stanislav.forms.person.AddPersonForm;
import org.stanislav.forms.person.EditPersonForm;
import org.stanislav.models.Person;
import org.stanislav.services.PeopleService;
import org.stanislav.util.PersonValidator;

import javax.validation.Valid;

/**
 * @author Stanislav Hlova
 */
@Controller
@RequestMapping(value = "/people", produces = "text/plain;charset=UTF-8")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("peopleList", peopleService.findAll());
        return "people/people";
    }

    @GetMapping("{id}")
    public String showPerson(@PathVariable("id") int id,
                             Model model) {
        Person person = peopleService.findById(id);
        model.addAttribute("person", person);
        model.addAttribute("takenBookList", peopleService.getBooksByPersonId(person.getId()));
        return "people/person";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("addPersonForm") AddPersonForm addPersonForm) {
        return "people/newPerson";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("addPersonForm") @Valid AddPersonForm addPersonForm,
                            BindingResult bindingResult) {
        Person person = addPersonForm.getModel();
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/newPerson";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id,
                             Model model) {
        model.addAttribute("editPersonForm", new EditPersonForm(peopleService.findById(id)));
        return "people/editPerson";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable("id") int id,
                               @ModelAttribute("editPersonForm") @Valid EditPersonForm editPersonForm,
                               BindingResult bindingResult) {
        Person person = editPersonForm.getModel();
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/editPerson";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
