package com.rodrigo.delivery.repository;

import com.rodrigo.delivery.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUidAndAtivo(String uid, boolean b);
}
