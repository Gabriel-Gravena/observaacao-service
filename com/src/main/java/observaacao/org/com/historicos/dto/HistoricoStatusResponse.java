package observaacao.org.com.historicos.dto;

import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.historicos.HistoricoStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoStatusResponse(
        UUID id,
        UUID solicitacaoId,
        StatusSolicitacao statusAnterior,
        StatusSolicitacao statusNovo,
        String comentario,
        UUID servidorId,
        String servidorNome,
        LocalDateTime createdAt
) {
    public HistoricoStatusResponse(HistoricoStatus historico) {
        this(
                historico.getId(),
                historico.getSolicitacao().getId(),
                historico.getStatusAnterior(),
                historico.getStatusNovo(),
                historico.getComentario(),
                historico.getServidor() != null ? historico.getServidor().getId() : null,
                historico.getServidor() != null ? historico.getServidor().getName() : null,
                historico.getCreatedAt()
        );
    }
}
