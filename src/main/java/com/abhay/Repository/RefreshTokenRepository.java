package com.abhay.Repository;

import com.abhay.Entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken>  findByToken(String token);
    @Transactional
    void deleteByEmail(String email);

}
