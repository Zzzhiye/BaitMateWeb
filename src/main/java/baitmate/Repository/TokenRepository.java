package baitmate.Repository;

import baitmate.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    long countByActive(boolean active);
    List<Token> findAllByExpiryDateBefore(LocalDateTime date);
}
