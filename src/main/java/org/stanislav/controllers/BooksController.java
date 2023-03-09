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
import org.stanislav.dao.BookDao;
import org.stanislav.dao.PersonDao;
import org.stanislav.forms.book.AddBookForm;
import org.stanislav.forms.book.EditBookForm;
import org.stanislav.models.Book;
import org.stanislav.models.Person;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Stanislav Hlova
 */
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDao bookDao;
    private final PersonDao personDao;

    @Autowired
    public BooksController(BookDao bookDao, PersonDao personDao) {
        this.bookDao = bookDao;
        this.personDao = personDao;
    }

    @GetMapping()
    public String showBooks(Model model) {
        model.addAttribute("bookList", bookDao.readAll());
        return "books/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("book", bookDao.read(id));
        Optional<Person> owner = bookDao.readBookOwner(id);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("peopleList", personDao.readAllPersons());
        }

        return "books/book";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("addBookForm") AddBookForm addBookForm) {
        return "books/newBook";
    }

    @PostMapping()
    public String addBook(@ModelAttribute("addBookForm") @Valid AddBookForm addBookForm,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/newBook";
        }
        bookDao.create(addBookForm.getModel());
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("editBookForm", new EditBookForm(bookDao.read(id)));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("editBookForm") @Valid EditBookForm editBookForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/editBook";
        }
        bookDao.update(id, editBookForm.getModel());
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDao.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDao.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id,
                             @ModelAttribute("person") Person person) {
        bookDao.assign(id, person);
        return "redirect:/books/" + id;
    }
}
