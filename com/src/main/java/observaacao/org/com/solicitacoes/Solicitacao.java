package observaacao.org.com.solicitacoes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import observaacao.org.com.categorias.Categoria;
import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitacoes")
@Getter
@NoArgsConstructor
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String protocolo;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private String bairro;

    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Column(nullable = false)
    private Boolean anonima;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cidadao_id")
        private User cidadao;

    private LocalDateTime prazoAlvo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Solicitacao(
            String protocolo,
            String titulo,
            String descricao,
            String bairro,
            String endereco,
            Prioridade prioridade,
            Boolean anonima,
            Categoria categoria,
            User cidadao
    ) {
        this.protocolo = protocolo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.bairro = bairro;
        this.endereco = endereco;
        this.prioridade = prioridade;
        this.anonima = anonima || categoria.getSensivel();
        this.categoria = categoria;
        this.cidadao = cidadao;
        this.status = StatusSolicitacao.ABERTO;
        this.createdAt = LocalDateTime.now();
        this.prazoAlvo = calcularPrazoAlvo(this.createdAt, prioridade);
    }

    public void update(
            String titulo,
            String descricao,
            String bairro,
            String endereco,
            Prioridade prioridade,
            Boolean anonima,
            Categoria categoria
    ) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.bairro = bairro;
        this.endereco = endereco;
        this.prioridade = prioridade;
        this.anonima = anonima || categoria.getSensivel();
        this.categoria = categoria;
        this.prazoAlvo = calcularPrazoAlvo(this.createdAt, prioridade);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(StatusSolicitacao status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    private LocalDateTime calcularPrazoAlvo(LocalDateTime base, Prioridade prioridade) {
        return switch (prioridade) {
            case BAIXA -> base.plusDays(15);
            case MEDIA -> base.plusDays(10);
            case ALTA -> base.plusDays(5);
            case CRITICA -> base.plusDays(2);
        };
    }
}
