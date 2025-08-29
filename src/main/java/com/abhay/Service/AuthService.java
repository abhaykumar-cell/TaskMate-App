package com.abhay.Service;

import com.abhay.Entity.MyUser;
import com.abhay.Entity.RefreshToken;
import com.abhay.Repository.RefreshTokenRepository;
import com.abhay.Repository.UserRepository;
import com.abhay.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private EmailService emailService;

    public static Integer generateOtp(){
        int otp = 100000 + new Random().nextInt(900000);
            return otp;

    }



    public void registerUser(String name , String email, String password,String role){

        MyUser user = new MyUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setRole(role.toUpperCase());
        userRepository.save(user);
    }

    public String generateJwtToken(String email){
        MyUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null){
            throw new IllegalArgumentException("User not found!");
        }
        return jwtUtil.generateToken(email, List.of(user.getRole()));
    }
    public MyUser getUser(String email){
        MyUser user = userRepository.findByEmail(email).orElse(null);
        System.out.println(user);
        System.out.println(user.getEmail());
        if (user == null){
            throw new IllegalArgumentException("User not found!");
        }
        return user;
    }
    public List<MyUser> getAllUsers(){
        return userRepository.findAll();
    }
    public String generateRefreshToken(String email){
        return refreshTokenService.createRefreshToken(email);
    }

    public String refreshJwtToken(String refreshToken){
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        return generateJwtToken(token.getEmail());

    }
}
