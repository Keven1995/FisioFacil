package br.com.kevenaraujo.fisiofacil.usecase.planos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.kevenaraujo.fisiofacil.dto.MembroPlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.PlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@ExtendWith(MockitoExtension.class)
class PlanosUseCasesTest {

    @Mock
    private PlanosService planosService;

    @Mock
    private MembroPlanoRepository membroPlanoRepository;

    @Test
    void listPlanosUseCaseDeveRetornarPlanosDoService() {
        ListPlanosUseCase useCase = new ListPlanosUseCase(planosService);
        Planos plano = new Planos();
        plano.setId(1L);
        plano.setNome("Basico");
        plano.setDescricao("Plano inicial");
        plano.setPreco(39.9);
        when(planosService.listarPlanos()).thenReturn(List.of(plano));

        List<PlanoResponseDTO> resultado = useCase.execute();

        assertEquals(1, resultado.size());
        assertEquals("Basico", resultado.get(0).getNome());
        assertEquals(39.9, resultado.get(0).getPreco());
    }

    @Test
    void listMembrosPorPlanoUseCaseDeveRetornarRelacoesDoRepositorio() {
        ListMembrosPorPlanoUseCase useCase = new ListMembrosPorPlanoUseCase(membroPlanoRepository);
        Membro membro = new Membro();
        membro.setId(10L);
        membro.setNome("Perna");

        Planos plano = new Planos();
        plano.setId(5L);
        plano.setNome("Plus");
        plano.setDescricao("Plano plus");

        MembroPlano relacao = new MembroPlano();
        relacao.setMembro(membro);
        relacao.setPlano(plano);

        when(membroPlanoRepository.findByPlanoId(1L)).thenReturn(List.of(relacao));

        List<MembroPlanoResponseDTO> resultado = useCase.execute(1L);

        assertEquals(1, resultado.size());
        assertEquals("Perna", resultado.get(0).getMembroNome());
        assertEquals("Plus", resultado.get(0).getPlanoNome());
    }
}
