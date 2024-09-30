package com.example.SpringMVN.Repository;

import com.example.SpringMVN.Model.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserModel, ObjectId> {
    Optional<UserModel> findByUsername(String username);
}
