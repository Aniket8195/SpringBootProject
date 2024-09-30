package com.example.SpringMVN.Model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "Books")

public class BookModel {
    @Id
    private String id;
    private String title;
    private String author;
    private double price;

}
