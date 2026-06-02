package observaacao.org.com.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull Boolean sensivel,
        Boolean ativa
) {
}
