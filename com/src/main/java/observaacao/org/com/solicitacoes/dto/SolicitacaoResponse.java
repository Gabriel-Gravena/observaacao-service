package observaacao.org.com.solicitacoes.dto;

import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.solicitacoes.Solicitacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoResponse(
        UUID id,
        String protocolo,
        String titulo,
        String descricao,
        String bairro,
        String endereco,
        StatusSolicitacao status,
        Prioridade prioridade,
        Boolean anonima,
        UUID categoriaId,
        String categoriaNome,
        UUID cidadaoId,
        String cidadaoNome,
        LocalDateTime prazoAlvo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public SolicitacaoResponse(Solicitacao solicitacao) {
        this(
                solicitacao.getId(),
                solicitacao.getProtocolo(),
                solicitacao.getTitulo(),
                solicitacao.getDescricao(),
                solicitacao.getBairro(),
                solicitacao.getEndereco(),
                solicitacao.getStatus(),
                solicitacao.getPrioridade(),
                solicitacao.getAnonima(),
                solicitacao.getCategoria().getId(),
                solicitacao.getCategoria().getNome(),
                solicitacao.getCidadao().getId(),
                solicitacao.getAnonima() ? null : solicitacao.getCidadao().getName(),
                solicitacao.getPrazoAlvo(),
                solicitacao.getCreatedAt(),
                solicitacao.getUpdatedAt()
        );
    }
}
