package com.example.SpringMVN.Controller;


import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Repository.UserRepo;
import com.example.SpringMVN.Service.JwtUtil;
import com.example.SpringMVN.Service.PasswordService;
import com.example.SpringMVN.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>>getUsers(){
        try {
            List<UserModel>users=userService.getUsers();
            return new ResponseEntity<>(users,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserModel user){
       try{

           if (user.getUsername().isEmpty()) {
               return ResponseEntity.badRequest().body("Username cannot be null or empty.");
           }

           if (user.getPassword().isEmpty()) {
               return ResponseEntity.badRequest().body("Password cannot be null or empty.");
           }

           Optional<UserModel> existingUser = userRepo.findByUsername(user.getUsername());
           if (existingUser.isPresent()) {
               return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
           }

           user.setPassword(passwordService.hashPassword(user.getPassword()));
           userRepo.save(user);
           return ResponseEntity.status(200).body("User Created");
       }catch (Exception exception){
           System.out.println(exception.getMessage());
           return new ResponseEntity<>("Error Occurred", HttpStatus.BAD_REQUEST);
       }

    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserModel user) {
        try {
            Optional<UserModel> existingUserOpt = userService.getUserByUsername(user.getUsername());

            if (existingUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: User not found");
            }

            UserModel existingUser = existingUserOpt.get(); // Safely retrieve the user
            if (!passwordService.checkPassword(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: Incorrect password");
            }
            String token = jwtUtil.generateToken(existingUser.getUsername().concat(existingUser.getId().toString()));
            return ResponseEntity.ok(token);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred: " + exception.getMessage());
        }
    }

}
