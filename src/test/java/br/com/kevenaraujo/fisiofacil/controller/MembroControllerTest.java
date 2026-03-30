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
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;

@WebMvcTest(MembroController.class)
@AutoConfigureMockMvc(addFilters = false)
class MembroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MembroService membroService;

    @MockBean
    private MembroPlanoRepository membroPlanoRepository;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void listarMembrosPorCategoriaDeveRetornarDtoSemDadosInternosDaEntidade() throws Exception {
        Membro membro = new Membro();
        membro.setId(1L);
        membro.setNome("Joelho");

        when(membroService.listarMembrosPorCategoria(2L)).thenReturn(List.of(membro));

        mockMvc.perform(get("/api/membros/categoria/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Joelho"))
                .andExpect(jsonPath("$[0].categoria").doesNotExist());
    }
}
