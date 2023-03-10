package com.rochelle.books_crud_pt2.controllers;
//? Pair Programming -> Dominic Basa
// import javax.servlet.http.HttpSession;

// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rochelle.books_crud_pt2.models.Book;
import com.rochelle.books_crud_pt2.services.BookService;


// to make this a controller need the @Controller anotation
// all the routes are handled by the controller 
@Controller
public class MainController {
    // ~ Now can go into controller and do this for now b/c do not have databases yet ~
    // this will be a list of all the books
    // import ArrayList -> import java.util.ArrayList;
            // import POJO judt made -> import com.rochelle.books.models.Book;
    //? instead of storing books in an arraylist -> connect to my service
    
    // ArrayList<Book> books = new ArrayList<>();
    // now when create book still setting it to session but will also create an instance of that book -> create a book object 
    
    // the last step is the controller and it uses the service
    // now will get all my books from my DB -> go to DB and create some 
    // now DB has some books in it -> use my service to access my books 
    // connects the service to the controller
    @Autowired BookService bookService;


    //CRUD -> create, read, update, delete
    //& CREATE &
    /* 
    ? want to create a book -> need 2 routes 
    -> one for the submission 
    -> one for the redirect  
    */
    
    @RequestMapping("/books/new") 
    // if do this will have to modify new.jsp
    public String newBook(@ModelAttribute("book")Book book){
        return "new.jsp";
    } 
    
    //? Instead of taking RequestParams will pass in actual book 
    // the form will be filled out
    // spring will make the book for us 
    // then send it back to the controller 
    // ~ need to use the @ModelAttribute in line 50 to do this 

    // @RequestMapping(value = "/books", method=RequestMethod.POST)
    // public String createBook() {
    //     return "redirect:/";
    // }
    //? Can change from RequestMapping to PostMapping which is a shorter method -> less to rememeber
    // it is the same as the above
    // this is the route that handles my book
    @PostMapping("/books")
    public String createBook(
        //~ when go to form and fill out book, how to access data ~
        // -> now have access to this data coming into my form
        // @RequestParam("title") String title,
        // @RequestParam("author") String author,
        // @RequestParam("pages") Integer pages,
        // @RequestParam("description") String description,
        // HttpSession session
        
        /* 
        ~ this is much cleaner
        -> set up the ModelAttribute 
        -> pass in the empty book, once it gets to the new.jsp 
        -> it gives the form a book to work with -> makes book on the fly 
        -> once get back to controller 
        -> pass it to our book service
        
        */
        @ModelAttribute("book")Book book
    ) {
        // when fill out the form -> get all this stuff that comes in on the form
        // make a book -> pass it on to my service -> service then pass it on to repo -> repo then puts it in my database
        // Book newBook = new Book(title, author, pages, description);
        // in the service making this method
        // invoke the method here -> arguement is newBook -> book instanced
        // bookService.createBook(newBook);
        // can pass in the book object that i get from the form
        bookService.createBook(book);
        return "redirect:/";
    }


    //? For read -> read one and read all 
    //& READ ALL & 
    // -> usually root of app is the read all 
    /* 
    ~ on index page ~
    -> want to loop through on the index page and whatever books are in my list of books i want to store them on the index page
    -> want to have a table of all the data
    -> want to store the books I have created
    -> send my whole books array and send them to my index page
    -> take array of books and send down to my index.jsp -> need to import model to send data to my template -> import org.springframework.ui.Model;
    -> to see if this works go to index page andn <c:out />  the array of books 
    */
    @RequestMapping("/")
    public String index(Model model) {
        // -> pass down books and call it books
        // to get all my books -> say it is going to be a list of books
        List<Book> books = bookService.allBooks();
        // to see what we get
        System.out.println(books); // books i created in the DB show in ther terminal 
        // to get them on the frontend -> from the controller to the view
        model.addAttribute("books", books);
        return "index.jsp";
    }


    //& READ ONE & 
    /* 
    -> when do this show.jsp should have access to one book at the specified index
    -> have one book object that is being passed down to jsp
    attributes can be individual strings, they can be books, they can be whatever you want to pass down
    -> want to click on one of these titles and have it take me to a show page
    -> can use request mapping or get mapping 
    */
    @GetMapping("/books/{id}")
    // -> now just need to call the method created in service to read one 
    // get variables that live in the url -> Path Variable
    public String show(@PathVariable("id")Long id, Model model) {
        // need to get my book to my show page -> link data in my controller to my template -> Model model
        Book book = bookService.getOneBook(id);
        model.addAttribute("book", book);
        return "show.jsp";
    }
    

    //& UPDATE & 
    /* 
    putting stuff on my DB -> 2 routes
    -> one to display the form and one to handle the form 
    */
    @GetMapping("/books/edit/{id}")
    public String edit(@PathVariable("id")Long id, Model model) {
        // the edit page is often similar to the new page so copy it and change a few things
        // samething as read one since need to view one book to edit it
        // so can use that code as well
        Book book = bookService.getOneBook(id);
        model.addAttribute("book", book);
        return "edit.jsp";
    }

    /* 
    -> need to make a route for the edit
    */

    @PutMapping("/books/{id}")
    // now need a service 
    public String update(@ModelAttribute("book")Book book) {
        bookService.updateBook(book);
        return "redirect:/";
    }
    
    //& DELETE & 
    @DeleteMapping("/books/{id}")
    // want to delete whatever book has this variable 
    public String obliterateBook(@PathVariable("id")Long id) {
        // need to find the book 1st that has that id
        Book book = bookService.getOneBook(id);
        // once have id -> delete book by id and pass in id
        bookService.deleteBook(book);
        // no where else to go after delete book -> no show page for that book
        return "redirect:/";
    }

}
