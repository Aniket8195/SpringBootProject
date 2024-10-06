package com.example.SpringMVN.Service;

import com.example.SpringMVN.Model.BookModel;
import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Repository.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    UserService userService;

    @Autowired
    BookRepo bookRepo;


    private static final Logger logger= LoggerFactory.getLogger(BookService.class);

    @Transactional
    public BookModel addBook(BookModel book, String username){
        try {
            BookModel newBook = bookRepo.save(book);
            Optional<UserModel> user = userService.getUserByUsername(username);
            if (user.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            UserModel existingUser = user.get();
            existingUser.getBooks().add(newBook);

            userService.saveUser(existingUser);

            return newBook;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Failed to add book", e);
        }
    }

    @Transactional
    public void deleteBook(String id,String username){
        try{
            Optional<UserModel> user=userService.getUserByUsername(username);


            if(user.isEmpty())throw new Exception("user Not found");
            Optional<BookModel> book=bookRepo.findById(id);

            if(book.isEmpty())throw new Exception("Book Not found");
            UserModel existingUser = user.get();
            BookModel existingBook = book.get();
            existingUser.getBooks().remove(existingBook);
            userService.saveUser(existingUser);
            bookRepo.deleteById(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw  new RuntimeException("Failed to add book", e);
        }
    }
}
