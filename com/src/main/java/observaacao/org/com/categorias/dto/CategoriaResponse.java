package observaacao.org.com.categorias.dto;

import observaacao.org.com.categorias.Categoria;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponse(
        UUID id,
        String nome,
        String descricao,
        Boolean sensivel,
        Boolean ativa,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public CategoriaResponse(Categoria categoria) {
        this(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getSensivel(),
                categoria.getAtiva(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );
    }
}
