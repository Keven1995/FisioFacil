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

import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.repository.MembroRepository;

@ExtendWith(MockitoExtension.class)
class MembroServiceTest {

    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private MembroService membroService;

    @Test
    void listarMembrosPorCategoriaDeveRetornarDadosDoRepositorio() {
        Membro membro = new Membro();
        membro.setNome("Ombro");
        when(membroRepository.findByCategoriaId(1L)).thenReturn(List.of(membro));

        List<Membro> resultado = membroService.listarMembrosPorCategoria(1L);

        assertEquals(1, resultado.size());
        assertEquals("Ombro", resultado.get(0).getNome());
    }

    @Test
    void buscarPorIdDeveRetornarOptionalDoRepositorio() {
        Membro membro = new Membro();
        membro.setId(3L);
        when(membroRepository.findById(3L)).thenReturn(Optional.of(membro));

        Optional<Membro> resultado = membroService.buscarPorId(3L);

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getId());
    }
}
