package com.example.SpringMVN.Controller;
import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Repository.BookRepo;
import com.example.SpringMVN.Service.BookService;
import com.example.SpringMVN.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.SpringMVN.Model.BookModel;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    private static final Logger logger= LoggerFactory.getLogger(BookService.class);

    @PostMapping
    public ResponseEntity<BookModel> addBook(@RequestBody BookModel book,@RequestParam String username){
        try {
            BookModel newBook = bookService.addBook(book, username);
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<BookModel>> getBooks(){
        List<BookModel>res=bookRepo.findAll();
        if(!res.isEmpty()){
            logger.error("No Books in DB");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<BookModel>> getBook(@PathVariable String id){
        Optional<BookModel> res=bookRepo.findById(id);
        if(res.isPresent()){
            logger.error("Books not found for user{id}");
            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookModel> updateBook(@PathVariable String id, @RequestBody BookModel bookDetails) {
        return bookRepo.findById(id)
                .map(existingBook -> {

                    if (bookDetails.getTitle() != null) {
                        existingBook.setTitle(bookDetails.getTitle());
                    }
                    if (bookDetails.getAuthor() != null) {
                        existingBook.setAuthor(bookDetails.getAuthor());
                    }
                    existingBook.setPrice(bookDetails.getPrice());

                    BookModel updatedBook = bookRepo.save(existingBook);

                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id,@RequestParam String username) {
        try {
            bookService.deleteBook(id,username);

            return new ResponseEntity<>("Book Deleted",HttpStatus.OK);
        }catch (Exception exception){
            logger.error(exception.getMessage());
            return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
        }
    }

}
