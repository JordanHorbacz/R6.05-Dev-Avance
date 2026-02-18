package org.univ_paris8.iut.montreuil.dev_avance.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {

    private static final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public static String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, userId);
        return token;
    }

    public static boolean validateToken(String token) {
        return tokens.containsKey(token);
    }

    public static Long getUserIdFromToken(String token) {
        return tokens.get(token);
    }

    public static void removeToken(String token) {
        tokens.remove(token);
    }
}
