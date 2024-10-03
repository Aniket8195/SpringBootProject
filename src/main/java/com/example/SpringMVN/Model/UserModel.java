package com.example.SpringMVN.Model;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;


import java.util.ArrayList;
import java.util.List;

@Document(collection = "User")
@Getter
@Setter
@Data
@NoArgsConstructor
public class UserModel{
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    @DBRef
    private List<BookModel> books=new ArrayList<>();
    @NonNull
    private List<String> role= new ArrayList<>();

}
