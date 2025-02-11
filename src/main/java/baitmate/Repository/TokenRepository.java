package baitmate.Repository;

import baitmate.model.Token;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
  Token findByToken(String token);

  long countByActive(boolean active);

  List<Token> findAllByExpiryDateBefore(LocalDateTime date);
}
