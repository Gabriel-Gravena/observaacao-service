package observaacao.org.com.solicitacoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import observaacao.org.com.common.enums.StatusSolicitacao;

public record AtualizarStatusRequest(
        @NotNull StatusSolicitacao status,
        @NotBlank String comentario
) {
}
