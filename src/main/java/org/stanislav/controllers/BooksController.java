package org.stanislav.controllers;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.stanislav.forms.book.AddBookForm;
import org.stanislav.forms.book.EditBookForm;
import org.stanislav.models.Book;
import org.stanislav.models.Person;
import org.stanislav.services.BooksService;
import org.stanislav.services.PeopleService;

import java.util.Collections;

/**
 * @author Stanislav Hlova
 */
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showBooks(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                            @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                            Model model) {
        if(page==null || booksPerPage == null) {
            model.addAttribute("bookList", booksService.getAllBooks(sortByYear));
        }else {
            model.addAttribute("bookList", booksService.getPaginatedBooks(page,booksPerPage,sortByYear));
        }
        return "books/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id,
                           Model model) {
        Book book = booksService.getBook(id);
        model.addAttribute("book", book);
        Person owner = book.getOwner();
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("peopleList", peopleService.findAll());
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
        booksService.save(addBookForm.getModel());
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("editBookForm", new EditBookForm(booksService.getBook(id)));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("editBookForm") @Valid EditBookForm editBookForm,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/editBook";
        }
        booksService.update(id, editBookForm.getModel());
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id,
                             @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam(value = "query", required = false) String query,
                             Model model) {
        if (query == null) {
            return "books/search";
        }
        if (query.isBlank()){
            model.addAttribute("foundedBooks", Collections.emptyList());
            return "books/search";
        }
        model.addAttribute("query",query);
        model.addAttribute("foundedBooks", booksService.searchByTitleStartingWith(query));
        return "books/search";
    }
}
