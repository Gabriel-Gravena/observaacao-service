package observaacao.org.com.solicitacoes;

import jakarta.validation.Valid;
import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.historicos.HistoricoStatusService;
import observaacao.org.com.historicos.dto.HistoricoStatusResponse;
import observaacao.org.com.solicitacoes.dto.AtualizarStatusRequest;
import observaacao.org.com.solicitacoes.dto.SolicitacaoRequest;
import observaacao.org.com.solicitacoes.dto.SolicitacaoResponse;
import observaacao.org.com.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService solicitacaoService;
    private final HistoricoStatusService historicoStatusService;

    public SolicitacaoController(
            SolicitacaoService solicitacaoService,
            HistoricoStatusService historicoStatusService
    ) {
        this.solicitacaoService = solicitacaoService;
        this.historicoStatusService = historicoStatusService;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> create(
            @RequestBody @Valid SolicitacaoRequest request,
            @AuthenticationPrincipal User user
    ) {
        Solicitacao solicitacao = solicitacaoService.create(request, user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(solicitacao.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new SolicitacaoResponse(solicitacao));
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponse>> findAll(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) Prioridade prioridade
    ) {
        List<SolicitacaoResponse> solicitacoes = solicitacaoService
                .filter(bairro, categoriaId, status, prioridade)
                .stream()
                .map(SolicitacaoResponse::new)
                .toList();

        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<SolicitacaoResponse>> findMinhas(@AuthenticationPrincipal User user) {
        List<SolicitacaoResponse> solicitacoes = solicitacaoService.findByCidadao(user)
                .stream()
                .map(SolicitacaoResponse::new)
                .toList();

        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<SolicitacaoResponse>> findAtrasadas() {
        List<SolicitacaoResponse> solicitacoes = solicitacaoService.findAtrasadas()
                .stream()
                .map(SolicitacaoResponse::new)
                .toList();

        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<SolicitacaoResponse> findByProtocolo(@PathVariable String protocolo) {
        return ResponseEntity.ok(new SolicitacaoResponse(solicitacaoService.findByProtocolo(protocolo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(new SolicitacaoResponse(solicitacaoService.findByIdForUser(id, user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid SolicitacaoRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(new SolicitacaoResponse(solicitacaoService.update(id, request, user)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SolicitacaoResponse> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarStatusRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(new SolicitacaoResponse(solicitacaoService.updateStatus(id, request, user)));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoStatusResponse>> findHistorico(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        solicitacaoService.findByIdForUser(id, user);

        List<HistoricoStatusResponse> historico = historicoStatusService.findBySolicitacaoId(id)
                .stream()
                .map(HistoricoStatusResponse::new)
                .toList();

        return ResponseEntity.ok(historico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        solicitacaoService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
