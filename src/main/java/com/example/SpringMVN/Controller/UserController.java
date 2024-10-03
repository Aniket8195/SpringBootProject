package com.example.SpringMVN.Controller;


import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Model.UserSignUpDTO;
import com.example.SpringMVN.Repository.UserRepo;
import com.example.SpringMVN.Service.JwtUtil;
import com.example.SpringMVN.Service.PasswordService;
import com.example.SpringMVN.Service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDTO userSignUpDTO) {
        try {
            if (userSignUpDTO.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body("Username cannot be null or empty.");
            }

            if (userSignUpDTO.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password cannot be null or empty.");
            }

            Optional<UserModel> existingUser = userRepo.findByUsername(userSignUpDTO.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
            }

            UserModel user = new UserModel();
            user.setUsername(userSignUpDTO.getUsername());
            user.setPassword(passwordService.hashPassword(userSignUpDTO.getPassword()));

            //user.getRole().add("admin");
            user.getRole().add("user");

            userRepo.save(user);
            Map<String,String>mp=new HashMap<>();
            mp.put("message","User Created");
            return ResponseEntity.ok(mp);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>("Error Occurred", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserSignUpDTO userSignUpDTO) {
        try {
            Optional<UserModel> existingUserOpt = userService.getUserByUsername(userSignUpDTO.getUsername());

            if (existingUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: User not found");
            }

            UserModel existingUser = existingUserOpt.get();
            if (!passwordService.checkPassword(userSignUpDTO.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: Incorrect password");
            }


            String token = jwtUtil.generateToken(existingUser.getUsername().concat(existingUser.getId().toString()),existingUser.getRole().get(0));
//            Cookie jwtCookie = new Cookie("jwt", token);
//            jwtCookie.setHttpOnly(true);
//            jwtCookie.setSecure(true);
//            jwtCookie.setPath("/");
//            jwtCookie.setMaxAge(7 * 24 * 60 * 60);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Authentication successful");

            return ResponseEntity.ok(response);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred: " + exception.getMessage());
        }
    }


}
