package observaacao.org.com.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import observaacao.org.com.common.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String cpf;
    private String telefone;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    public User(String name, String email, String cpf, String telefone, String password, Role role) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
}
