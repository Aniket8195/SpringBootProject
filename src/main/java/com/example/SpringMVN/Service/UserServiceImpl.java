package com.example.SpringMVN.Service;

import com.example.SpringMVN.Model.UserModel;
import com.example.SpringMVN.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userOptional = userRepo.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserModel user = userOptional.get();
        System.out.println("User Roles: " + user.getRole());

        List<GrantedAuthority> authorities = new ArrayList<>();

       user.getRole().forEach(role->{
           authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
       });
        System.out.println("authorities: " + authorities);

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
