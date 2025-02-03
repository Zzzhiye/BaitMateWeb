package baitmate.Service;

import baitmate.model.User;

public interface TokenService {
   String generateToken(User user);
   boolean validateToken(String token);
   void deactivateToken(String token);
   long countActiveSessions();

}
