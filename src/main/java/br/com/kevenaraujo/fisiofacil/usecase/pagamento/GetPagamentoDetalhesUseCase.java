package br.com.kevenaraujo.fisiofacil.usecase.pagamento;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.MembroPagamentoDTO;
import br.com.kevenaraujo.fisiofacil.dto.PagamentoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Categoria;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.CategoriaRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@Service
public class GetPagamentoDetalhesUseCase {

    private final PlanosService planosService;
    private final MembroService membroService;
    private final CategoriaRepository categoriaRepository;

    public GetPagamentoDetalhesUseCase(
            PlanosService planosService,
            MembroService membroService,
            CategoriaRepository categoriaRepository) {
        this.planosService = planosService;
        this.membroService = membroService;
        this.categoriaRepository = categoriaRepository;
    }

    public PagamentoResponseDTO execute(Long planoId, Long membroId) {
        Planos plano = planosService.buscarPorId(planoId)
                .orElseThrow(() -> new IllegalArgumentException("Plano ou membro nao encontrado"));

        Membro membro = membroService.buscarPorId(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Plano ou membro nao encontrado"));

        String descricao = "Plano: " + plano.getNome() + ", Membro: " + membro.getNome();
        PagamentoResponseDTO response = new PagamentoResponseDTO(descricao);

        if (PlanoPagamento.PLUS.matches(plano.getNome())) {
            Long categoriaSuperioresId = buscarCategoriaId(CategoriaPagamento.SUPERIORES);
            Long categoriaInferioresId = buscarCategoriaId(CategoriaPagamento.INFERIORES);

            List<Membro> membrosSuperiores = membroService.listarMembrosPorCategoria(categoriaSuperioresId);
            List<Membro> membrosInferiores = membroService.listarMembrosPorCategoria(categoriaInferioresId);
            response.setMembrosSuperiores(membrosSuperiores.stream().map(MembroPagamentoDTO::new).toList());
            response.setMembrosInferiores(membrosInferiores.stream().map(MembroPagamentoDTO::new).toList());
        }

        return response;
    }

    private Long buscarCategoriaId(CategoriaPagamento categoriaPagamento) {
        Categoria categoria = categoriaRepository.findFirstByNomeContainingIgnoreCase(categoriaPagamento.nomeBusca())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Categoria nao encontrada para: " + categoriaPagamento.name()));
        return categoria.getId();
    }
}
