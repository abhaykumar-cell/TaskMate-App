package com.abhay.Service;

import com.abhay.Entity.OTP;
import com.abhay.Repository.OTPRepo;
import com.abhay.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OTPService {
    @Autowired
    private OTPRepo otpRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public Integer addOtp(OTP otp){

        if (otpRepo.findByEmail(otp.getEmail()).isPresent() || userRepository.findByEmail(otp.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email Already exists!");
        }
        if (!otp.getRole().equalsIgnoreCase("ROLE_USER") && !otp.getRole().equalsIgnoreCase("ROLE_ADMIN")){
            throw new IllegalArgumentException("Invalid role!");
        }
        OTP newOtp = new OTP(otp.getName(),otp.getEmail(),otp.getRole(),passwordEncoder.encode(otp.getPassword()));
        Integer otpnum = 100000+new Random().nextInt(900000);

        newOtp.setOtp(otpnum);

        OTP res =otpRepo.save(newOtp);
        return otpnum;
    }
    public boolean deleteOtp(OTP otp){
        otpRepo.delete(otp);
        return true;
    }
    public OTP findUserByOtpAndEmail(String email, Integer otp){

        return otpRepo.findByEmailAndOtp(email, otp).orElse(null);
    }
    @Scheduled(fixedRate = 2000*60)
    public void deleteSchduledOTP(){
        int res =otpRepo.deleteByCreatedAtBefore(LocalDateTime.now().minusMinutes(2));
        System.out.println(res +"Rows deleted successfull");

    }


}
