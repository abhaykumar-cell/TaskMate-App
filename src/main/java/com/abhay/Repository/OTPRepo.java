package com.abhay.Repository;

import com.abhay.Entity.OTP;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OTPRepo extends JpaRepository<OTP,Integer> {

    Optional<OTP> findByEmailAndOtp(String email, Integer otp);
    Optional<OTP> findByEmail(String email);
    @Transactional
    @Modifying
    int deleteByCreatedAtBefore(LocalDateTime now);
}
