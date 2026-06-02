package observaacao.org.com.historicos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, UUID> {
    List<HistoricoStatus> findBySolicitacaoIdOrderByCreatedAtAsc(UUID solicitacaoId);
}
