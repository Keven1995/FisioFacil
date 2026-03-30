package br.com.kevenaraujo.fisiofacil.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.kevenaraujo.fisiofacil.Configurations.JwtAuthenticationFilter;
import br.com.kevenaraujo.fisiofacil.dto.MembroPagamentoDTO;
import br.com.kevenaraujo.fisiofacil.dto.PagamentoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.usecase.pagamento.GetPagamentoDetalhesUseCase;

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPagamentoDetalhesUseCase getPagamentoDetalhesUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getPagamentoDetalhesDeveRetornarOk() throws Exception {
        PagamentoResponseDTO dto = new PagamentoResponseDTO("Plano: Plus, Membro: Ombro");
        Membro membroEntity = new Membro();
        membroEntity.setId(1L);
        membroEntity.setNome("Ombro");
        dto.setMembrosSuperiores(List.of(new MembroPagamentoDTO(membroEntity)));

        when(getPagamentoDetalhesUseCase.execute(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/api/pagamento/1/membro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Plano: Plus, Membro: Ombro"))
                .andExpect(jsonPath("$.membrosSuperiores[0].nome").value("Ombro"));
    }

    @Test
    void getPagamentoDetalhesDeveRetornarBadRequestQuandoUseCaseFalhar() throws Exception {
        when(getPagamentoDetalhesUseCase.execute(99L, 99L))
                .thenThrow(new IllegalArgumentException("Plano ou membro nao encontrado"));

        mockMvc.perform(get("/api/pagamento/99/membro/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Plano ou membro nao encontrado"));
    }
}
