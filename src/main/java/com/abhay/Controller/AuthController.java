package com.abhay.Controller;

import com.abhay.Dto.RegisterUser;
import com.abhay.Dto.VarifyOtpRequest;
import com.abhay.Entity.MyUser;
import com.abhay.Entity.OTP;
import com.abhay.Service.AuthService;
import com.abhay.Service.EmailService;
import com.abhay.Service.OTPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@Tag(name="Authentication APIs")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private EmailService emailService;

    @Operation(summary = "Register User By Email")
    @PostMapping("/auth/register")
    public ResponseEntity<String> login(@Valid @RequestBody RegisterUser request){
        OTP otp = new OTP(request.getName(),request.getEmail(),request.getRole(),request.getPassword());

        Integer otpnum = otpService.addOtp(otp);
        emailService.SendEmail(request.getEmail(),"Your TaskMate OTP for Verification","Hello "+request.getName()+",\n" +
                "\n" +
                "We received a request to verify your account on TaskMate.\n" +
                "\n" +
                "Your One-Time Password (OTP) is:\n" +
                ""+otpnum+"\n" +
                "\n" +
                "This OTP is valid for the next 5 minutes.  \n" +
                "Please do not share this code with anyone for security reasons.\n" +
                "\n" +
                "If you did not request this OTP, you can safely ignore this email.\n" +
                "\n" +
                "Best regards,  \n" +
                "The TaskMate Team");

        return ResponseEntity.ok("OTP Send Successfully!");
    }
    @Operation(summary = "Validate Your Email by OTP")
    @PostMapping("/auth/validateEmail")
    public ResponseEntity<?> varifyOtp(@Valid @RequestBody VarifyOtpRequest request){

        OTP otp = otpService.findUserByOtpAndEmail(request.getEmail(),request.getOtp());
        if (otp!=null){
            authService.registerUser(otp.getName(),otp.getEmail(),otp.getPassword(), otp.getRole());
            otpService.deleteOtp(otp);

            return new ResponseEntity<>("User Registration Successfully",HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("Please Input Valid Email and Otp.",HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Login for Get Refresh Token & Token")
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        System.out.println("Loging method called");
        String email = request.get("email");
        String password = request.get("password");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        String token = authService.generateJwtToken(email);
        String refreshToken = authService.generateRefreshToken(email);
        return ResponseEntity.ok(Map.of("token", token,"refreshToken", refreshToken));
    }
    @Operation(summary = "Get Your Acount/Profile")
    @PostMapping("/account")
    public ResponseEntity<MyUser> getAccount(){
        System.out.println("get Account called");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("email is working "+ email);
        MyUser user = authService.getUser(email);
        System.out.println("user is working ");
        return ResponseEntity.ok(user);
    }
    @Operation(summary = "Get all user , For ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<List<MyUser>> getAllUsers(){
        List<MyUser> user = authService.getAllUsers();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @Operation(summary = "Get token by refresh token")
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken){

        System.out.println("🔁 Refresh Token Received: " + refreshToken);
        String newJwt = authService.generateJwtToken(refreshToken);

        return ResponseEntity.ok(Map.of("token",newJwt));

    }

}
