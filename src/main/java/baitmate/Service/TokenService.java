package baitmate.Service;

import baitmate.model.User;

public interface TokenService {
  String generateToken(User user);

  boolean validateToken(String token);

  boolean deactivateToken(String token);

  long countActiveSessions();
}
