package observaacao.org.com.solicitacoes;

import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {
    Optional<Solicitacao> findByProtocolo(String protocolo);

    List<Solicitacao> findByCidadao(User cidadao);

    List<Solicitacao> findByBairroIgnoreCase(String bairro);

    List<Solicitacao> findByCategoriaId(UUID categoriaId);

    List<Solicitacao> findByStatus(StatusSolicitacao status);

    List<Solicitacao> findByPrioridade(Prioridade prioridade);

    List<Solicitacao> findByPrazoAlvoBeforeAndStatusNotIn(LocalDateTime data, List<StatusSolicitacao> status);

    @Query("""
            select s from Solicitacao s
            where (:bairro is null or lower(s.bairro) = lower(:bairro))
            and (:categoriaId is null or s.categoria.id = :categoriaId)
            and (:status is null or s.status = :status)
            and (:prioridade is null or s.prioridade = :prioridade)
            """)
    List<Solicitacao> findWithFilters(
            @Param("bairro") String bairro,
            @Param("categoriaId") UUID categoriaId,
            @Param("status") StatusSolicitacao status,
            @Param("prioridade") Prioridade prioridade
    );
}
