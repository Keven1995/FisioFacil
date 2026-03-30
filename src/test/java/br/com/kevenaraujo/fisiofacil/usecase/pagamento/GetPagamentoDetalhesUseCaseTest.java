package br.com.kevenaraujo.fisiofacil.usecase.pagamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.kevenaraujo.fisiofacil.dto.PagamentoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Categoria;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.CategoriaRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@ExtendWith(MockitoExtension.class)
class GetPagamentoDetalhesUseCaseTest {

    @Mock
    private PlanosService planosService;

    @Mock
    private MembroService membroService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void executeDeveRetornarDetalhesComListasQuandoPlanoForPlus() {
        GetPagamentoDetalhesUseCase useCase = new GetPagamentoDetalhesUseCase(planosService, membroService, categoriaRepository);

        Planos plano = new Planos();
        plano.setNome("Plus");
        Membro membro = new Membro();
        membro.setId(1L);
        membro.setNome("Ombro");
        Membro membroSuperior = new Membro();
        membroSuperior.setId(2L);
        membroSuperior.setNome("Cotovelo");
        Membro membroInferior = new Membro();
        membroInferior.setId(3L);
        membroInferior.setNome("Joelho");
        Categoria categoriaSuperior = new Categoria();
        categoriaSuperior.setId(10L);
        categoriaSuperior.setNome("Membro Superior");
        Categoria categoriaInferior = new Categoria();
        categoriaInferior.setId(20L);
        categoriaInferior.setNome("Membro Inferior");

        when(planosService.buscarPorId(1L)).thenReturn(Optional.of(plano));
        when(membroService.buscarPorId(1L)).thenReturn(Optional.of(membro));
        when(categoriaRepository.findFirstByNomeContainingIgnoreCase("superior")).thenReturn(Optional.of(categoriaSuperior));
        when(categoriaRepository.findFirstByNomeContainingIgnoreCase("inferior")).thenReturn(Optional.of(categoriaInferior));
        when(membroService.listarMembrosPorCategoria(10L)).thenReturn(List.of(membroSuperior));
        when(membroService.listarMembrosPorCategoria(20L)).thenReturn(List.of(membroInferior));

        PagamentoResponseDTO resultado = useCase.execute(1L, 1L);

        assertEquals("Plano: Plus, Membro: Ombro", resultado.getDescricao());
        assertEquals(1, resultado.getMembrosSuperiores().size());
        assertEquals(1, resultado.getMembrosInferiores().size());
        assertEquals("Cotovelo", resultado.getMembrosSuperiores().get(0).getNome());
        assertEquals(2L, resultado.getMembrosSuperiores().get(0).getId());
    }

    @Test
    void executeDeveLancarExcecaoQuandoPlanoOuMembroNaoExistirem() {
        GetPagamentoDetalhesUseCase useCase = new GetPagamentoDetalhesUseCase(planosService, membroService, categoriaRepository);
        when(planosService.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(99L, 99L));
    }
}
