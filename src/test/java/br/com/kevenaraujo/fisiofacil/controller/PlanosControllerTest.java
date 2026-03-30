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
import br.com.kevenaraujo.fisiofacil.dto.MembroPlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.PlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.usecase.planos.ListMembrosPorPlanoUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.planos.ListPlanosUseCase;

@WebMvcTest(PlanosController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlanosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListPlanosUseCase listPlanosUseCase;

    @MockBean
    private ListMembrosPorPlanoUseCase listMembrosPorPlanoUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void listarPlanosDeveRetornarOk() throws Exception {
        Planos entidadePlano = new Planos();
        entidadePlano.setId(1L);
        entidadePlano.setNome("Basico");
        entidadePlano.setDescricao("Plano inicial");
        entidadePlano.setPreco(49.9);

        PlanoResponseDTO plano = new PlanoResponseDTO(entidadePlano);
        when(listPlanosUseCase.execute()).thenReturn(List.of(plano));

        mockMvc.perform(get("/api/planos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Basico"))
                .andExpect(jsonPath("$[0].preco").value(49.9));
    }

    @Test
    void listarMembrosPorPlanoDeveRetornarOk() throws Exception {
        Membro membro = new Membro();
        membro.setId(7L);
        membro.setNome("Ombro");

        Planos plano = new Planos();
        plano.setId(1L);
        plano.setNome("Premium");
        plano.setDescricao("Plano completo");

        MembroPlano membroPlano = new MembroPlano();
        membroPlano.setMembro(membro);
        membroPlano.setPlano(plano);

        MembroPlanoResponseDTO dto = new MembroPlanoResponseDTO(membroPlano);
        when(listMembrosPorPlanoUseCase.execute(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/planos/1/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].membroNome").value("Ombro"))
                .andExpect(jsonPath("$[0].planoNome").value("Premium"));
    }
}
