package br.com.kevenaraujo.fisiofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.PlanosRepository;

@ExtendWith(MockitoExtension.class)
class PlanosServiceTest {

    @Mock
    private PlanosRepository planosRepository;

    @InjectMocks
    private PlanosService planosService;

    @Test
    void listarPlanosDeveRetornarDadosDoRepositorio() {
        Planos plano = new Planos();
        plano.setNome("Plus");
        when(planosRepository.findAll()).thenReturn(List.of(plano));

        List<Planos> resultado = planosService.listarPlanos();

        assertEquals(1, resultado.size());
        assertEquals("Plus", resultado.get(0).getNome());
    }

    @Test
    void buscarPorIdDeveRetornarOptionalDoRepositorio() {
        Planos plano = new Planos();
        plano.setId(1L);
        when(planosRepository.findById(1L)).thenReturn(Optional.of(plano));

        Optional<Planos> resultado = planosService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }
}
