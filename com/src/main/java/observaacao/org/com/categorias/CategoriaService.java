package observaacao.org.com.categorias;

import observaacao.org.com.categorias.dto.CategoriaRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria create(CategoriaRequest request) {
        if (categoriaRepository.findByNomeIgnoreCase(request.nome()).isPresent()) {
            throw new RuntimeException("Categoria ja cadastrada");
        }

        Categoria categoria = new Categoria(request.nome(), request.descricao(), request.sensivel());
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
    }

    public Categoria update(UUID id, CategoriaRequest request) {
        Categoria categoria = findById(id);
        Boolean ativa = request.ativa() != null ? request.ativa() : categoria.getAtiva();
        categoria.update(request.nome(), request.descricao(), request.sensivel(), ativa);
        return categoriaRepository.save(categoria);
    }

    public void delete(UUID id) {
        categoriaRepository.delete(findById(id));
    }
}
