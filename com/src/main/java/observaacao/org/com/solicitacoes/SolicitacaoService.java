package observaacao.org.com.solicitacoes;

import observaacao.org.com.categorias.Categoria;
import observaacao.org.com.categorias.CategoriaService;
import observaacao.org.com.common.enums.Prioridade;
import observaacao.org.com.common.enums.Role;
import observaacao.org.com.common.enums.StatusSolicitacao;
import observaacao.org.com.historicos.HistoricoStatus;
import observaacao.org.com.historicos.HistoricoStatusService;
import observaacao.org.com.solicitacoes.dto.AtualizarStatusRequest;
import observaacao.org.com.solicitacoes.dto.SolicitacaoRequest;
import observaacao.org.com.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository solicitacaoRepository;
    private final CategoriaService categoriaService;
    private final HistoricoStatusService historicoStatusService;

    public SolicitacaoService(
            SolicitacaoRepository solicitacaoRepository,
            CategoriaService categoriaService,
            HistoricoStatusService historicoStatusService
    ) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.categoriaService = categoriaService;
        this.historicoStatusService = historicoStatusService;
    }

    public Solicitacao create(SolicitacaoRequest request, User cidadao) {
        Categoria categoria = categoriaService.findById(request.categoriaId());
        Solicitacao solicitacao = new Solicitacao(
                gerarProtocolo(),
                request.titulo(),
                request.descricao(),
                request.bairro(),
                request.endereco(),
                request.prioridade(),
                request.anonima(),
                categoria,
                cidadao
        );

        Solicitacao saved = solicitacaoRepository.save(solicitacao);
        historicoStatusService.create(new HistoricoStatus(
                saved,
                null,
                StatusSolicitacao.ABERTO,
                "Solicitacao aberta",
                null
        ));

        return saved;
    }

    public List<Solicitacao> findAll() {
        return solicitacaoRepository.findAll();
    }

    public Solicitacao findById(UUID id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitacao nao encontrada"));
    }

    public Solicitacao findByIdForUser(UUID id, User user) {
        Solicitacao solicitacao = findById(id);
        validarAcessoUsuario(solicitacao, user);
        return solicitacao;
    }

    public Solicitacao findByProtocolo(String protocolo) {
        return solicitacaoRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new RuntimeException("Solicitacao nao encontrada"));
    }

    public List<Solicitacao> findByCidadao(User cidadao) {
        return solicitacaoRepository.findByCidadao(cidadao);
    }

    public List<Solicitacao> findAtrasadas() {
        return solicitacaoRepository.findByPrazoAlvoBeforeAndStatusNotIn(
                LocalDateTime.now(),
                List.of(StatusSolicitacao.RESOLVIDO, StatusSolicitacao.ENCERRADO)
        );
    }

    public List<Solicitacao> filter(
            String bairro,
            UUID categoriaId,
            StatusSolicitacao status,
            Prioridade prioridade
    ) {
        return solicitacaoRepository.findWithFilters(bairro, categoriaId, status, prioridade);
    }

    public Solicitacao update(UUID id, SolicitacaoRequest request, User user) {
        Solicitacao solicitacao = findByIdForUser(id, user);
        Categoria categoria = categoriaService.findById(request.categoriaId());

        solicitacao.update(
                request.titulo(),
                request.descricao(),
                request.bairro(),
                request.endereco(),
                request.prioridade(),
                request.anonima(),
                categoria
        );

        return solicitacaoRepository.save(solicitacao);
    }

    public Solicitacao updateStatus(UUID id, AtualizarStatusRequest request, User servidor) {
        Solicitacao solicitacao = findById(id);
        StatusSolicitacao statusAnterior = solicitacao.getStatus();

        validarFluxoStatus(statusAnterior, request.status());

        solicitacao.updateStatus(request.status());
        Solicitacao saved = solicitacaoRepository.save(solicitacao);

        historicoStatusService.create(new HistoricoStatus(
                saved,
                statusAnterior,
                request.status(),
                request.comentario(),
                servidor
        ));

        return saved;
    }

    public void delete(UUID id, User user) {
        Solicitacao solicitacao = findByIdForUser(id, user);
        solicitacaoRepository.delete(solicitacao);
    }

    private void validarAcessoUsuario(Solicitacao solicitacao, User user) {
        boolean isServidor = user.getRole() == Role.SERVIDOR;
        boolean isDono = solicitacao.getCidadao().getId().equals(user.getId());

        if (!isServidor && !isDono) {
            throw new RuntimeException("Acesso negado");
        }
    }

    private void validarFluxoStatus(StatusSolicitacao statusAtual, StatusSolicitacao novoStatus) {
        boolean valido = switch (statusAtual) {
            case ABERTO -> novoStatus == StatusSolicitacao.TRIAGEM;
            case TRIAGEM -> novoStatus == StatusSolicitacao.EM_EXECUCAO;
            case EM_EXECUCAO -> novoStatus == StatusSolicitacao.RESOLVIDO;
            case RESOLVIDO -> novoStatus == StatusSolicitacao.ENCERRADO;
            case ENCERRADO -> false;
        };

        if (!valido) {
            throw new RuntimeException("Fluxo de status invalido");
        }
    }

    private String gerarProtocolo() {
        String ano = String.valueOf(LocalDate.now().getYear());
        String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "OBS-" + ano + "-" + codigo;
    }
}
