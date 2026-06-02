package observaacao.org.com.historicos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.solicitacoes.Solicitacao;
import observaacao.org.com.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historicos_status")
@Getter
@NoArgsConstructor
public class HistoricoStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitacao_id")
    private Solicitacao solicitacao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao statusNovo;

    @Column(nullable = false, length = 1000)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "servidor_id")
    private User servidor;

    private LocalDateTime createdAt;

    public HistoricoStatus(
            Solicitacao solicitacao,
            StatusSolicitacao statusAnterior,
            StatusSolicitacao statusNovo,
            String comentario,
            User servidor
    ) {
        this.solicitacao = solicitacao;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.comentario = comentario;
        this.servidor = servidor;
        this.createdAt = LocalDateTime.now();
    }
}
