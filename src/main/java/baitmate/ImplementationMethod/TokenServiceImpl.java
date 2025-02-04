package baitmate.ImplementationMethod;

import baitmate.Repository.TokenRepository;
import baitmate.Service.TokenService;
import baitmate.model.Token;
import baitmate.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepo;

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        Token newToken = new Token();
        newToken.setToken(token);
        newToken.setUserId(user.getId());
        newToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        newToken.setActive(true); // Set token as active
        tokenRepo.save(newToken);
        return token;
    }

    public boolean validateToken(String token) {
        Token storedToken = tokenRepo.findByToken(token);
        return storedToken != null && storedToken.isActive() && storedToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public boolean deactivateToken(String token) {
        Token storedToken = tokenRepo.findByToken(token);
        storedToken.setActive(false);
        tokenRepo.save(storedToken);
        System.out.println(storedToken.isActive());
        return !storedToken.isActive();
    }

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deactivateExpiredTokens() {
        List<Token> expiredTokens = tokenRepo.findAllByExpiryDateBefore(LocalDateTime.now());
        for (Token token : expiredTokens) {
            token.setActive(false);
            tokenRepo.save(token);
        }
    }

    public long countActiveSessions() {
        return tokenRepo.countByActive(true);
    }
}
