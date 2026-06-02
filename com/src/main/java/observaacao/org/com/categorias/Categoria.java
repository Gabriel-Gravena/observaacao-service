package observaacao.org.com.categorias;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categorias")
@Getter
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private Boolean sensivel;

    @Column(nullable = false)
    private Boolean ativa;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Categoria(String nome, String descricao, Boolean sensivel) {
        this.nome = nome;
        this.descricao = descricao;
        this.sensivel = sensivel;
        this.ativa = true;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String nome, String descricao, Boolean sensivel, Boolean ativa) {
        this.nome = nome;
        this.descricao = descricao;
        this.sensivel = sensivel;
        this.ativa = ativa;
        this.updatedAt = LocalDateTime.now();
    }
}
