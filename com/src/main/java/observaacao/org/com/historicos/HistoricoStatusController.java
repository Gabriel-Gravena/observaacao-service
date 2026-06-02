package observaacao.org.com.historicos;

import observaacao.org.com.historicos.dto.HistoricoStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/historicos-status")
public class HistoricoStatusController {
    private final HistoricoStatusService historicoStatusService;

    public HistoricoStatusController(HistoricoStatusService historicoStatusService) {
        this.historicoStatusService = historicoStatusService;
    }

    @GetMapping
    public ResponseEntity<List<HistoricoStatusResponse>> findAll() {
        List<HistoricoStatusResponse> historicos = historicoStatusService.findAll()
                .stream()
                .map(HistoricoStatusResponse::new)
                .toList();

        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoStatusResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(new HistoricoStatusResponse(historicoStatusService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        historicoStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
