package baitmate.ImplementationMethod;

import baitmate.Repository.TokenRepository;
import baitmate.Service.TokenService;
import baitmate.model.Token;
import baitmate.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
  @Autowired private TokenRepository tokenRepo;

  public String generateToken(User user) {
    String token = UUID.randomUUID().toString();
    Token newToken = new Token();
    newToken.setToken(token);
    newToken.setUserId(user.getId());
    newToken.setExpiryDate(LocalDateTime.now().plusHours(1));
    newToken.setActive(true); 
    tokenRepo.save(newToken);
    return token;
  }

  public boolean validateToken(String token) {
    Token storedToken = tokenRepo.findByToken(token);
    return storedToken != null
        && storedToken.isActive()
        && storedToken.getExpiryDate().isAfter(LocalDateTime.now());
  }

  public boolean deactivateToken(String token) {
    Token storedToken = tokenRepo.findByToken(token);
    storedToken.setActive(false);
    tokenRepo.save(storedToken);
    System.out.println(storedToken.isActive());
    return !storedToken.isActive();
  }

  @Scheduled(fixedRate = 3600000) 
  public void deactivateExpiredTokens() {
    List<Token> expiredTokens = tokenRepo.findAllByExpiryDateBefore(LocalDateTime.now());

    if (expiredTokens == null || expiredTokens.isEmpty()) {
      System.out.println("No expired tokens found to deactivate.");
      return;
    }

    for (Token token : expiredTokens) {
      token.setActive(false);
      tokenRepo.save(token);
    }
  }

  public long countActiveSessions() {
    return tokenRepo.countByActive(true);
  }
}
