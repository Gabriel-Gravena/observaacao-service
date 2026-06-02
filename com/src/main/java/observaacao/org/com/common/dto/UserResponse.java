package observaacao.org.com.common.dto;

import observaacao.org.com.common.enums.Role;
import observaacao.org.com.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String cpf,
        String telefone,
        Role role,
        LocalDateTime createdAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getTelefone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}