package observaacao.org.com.auth.dto;

import observaacao.org.com.user.User;

public record AuthResponse(
        User user,
        String token
) {
}
