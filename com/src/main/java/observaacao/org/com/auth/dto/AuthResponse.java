package observaacao.org.com.auth.dto;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String token
) {
}
