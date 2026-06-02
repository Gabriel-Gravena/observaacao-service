package observaacao.org.com.historicos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoricoStatusService {
    private final HistoricoStatusRepository historicoStatusRepository;

    public HistoricoStatusService(HistoricoStatusRepository historicoStatusRepository) {
        this.historicoStatusRepository = historicoStatusRepository;
    }

    public HistoricoStatus create(HistoricoStatus historicoStatus) {
        return historicoStatusRepository.save(historicoStatus);
    }

    public List<HistoricoStatus> findAll() {
        return historicoStatusRepository.findAll();
    }

    public HistoricoStatus findById(UUID id) {
        return historicoStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historico nao encontrado"));
    }

    public List<HistoricoStatus> findBySolicitacaoId(UUID solicitacaoId) {
        return historicoStatusRepository.findBySolicitacaoIdOrderByCreatedAtAsc(solicitacaoId);
    }

    public void delete(UUID id) {
        historicoStatusRepository.delete(findById(id));
    }
}
