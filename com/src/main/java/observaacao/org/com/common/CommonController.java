package observaacao.org.com.common;

import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.Role;
import observaacao.org.com.common.enums.StatusSolicitacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/prioridades")
    public ResponseEntity<List<Prioridade>> prioridades() {
        return ResponseEntity.ok(List.of(Prioridade.values()));
    }

    @GetMapping("/status-solicitacao")
    public ResponseEntity<List<StatusSolicitacao>> statusSolicitacao() {
        return ResponseEntity.ok(List.of(StatusSolicitacao.values()));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> roles() {
        return ResponseEntity.ok(List.of(Role.values()));
    }
}
