package com.example.SpringMVN.Service;

import com.example.SpringMVN.Model.BookModel;
import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Repository.UserRepo;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<UserModel> getUserForSA(){
        Query query=new Query();
        query.addCriteria(Criteria.where("email").exists(true));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        return mongoTemplate.find(query,UserModel.class);

    }
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserModel saveUser(UserModel user) {
        return userRepo.save(user);
    }

    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<UserModel> getUserById(ObjectId id) {
        return userRepo.findById(id);
    }

    public List<BookModel> getBooks(UserModel user) {
        return user.getBooks();
    }

    public void deleteUser(ObjectId id) {
        userRepo.deleteById(id);
    }

    public boolean existsById(ObjectId id) {
        return userRepo.existsById(id);
    }

    public Optional<UserModel> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    public List<UserModel>getUsers(){
        return userRepo.findAll();
    }


}

