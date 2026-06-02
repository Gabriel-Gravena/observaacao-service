package observaacao.org.com.solicitacoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import observaacao.org.com.common.enums.Prioridade;

import java.util.UUID;

public record SolicitacaoRequest(
        @NotBlank String titulo,
        @NotBlank String descricao,
        @NotBlank String bairro,
        String endereco,
        @NotNull Prioridade prioridade,
        @NotNull Boolean anonima,
        @NotNull UUID categoriaId
) {
}
