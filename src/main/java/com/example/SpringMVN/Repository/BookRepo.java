package com.example.SpringMVN.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import com.example.SpringMVN.Model.BookModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends MongoRepository<BookModel,String> {

}
