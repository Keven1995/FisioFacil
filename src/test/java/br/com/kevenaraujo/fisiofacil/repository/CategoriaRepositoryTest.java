package br.com.kevenaraujo.fisiofacil.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.kevenaraujo.fisiofacil.entity.Categoria;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void findFirstByNomeContainingIgnoreCaseDeveRetornarCategoriaQuandoExistir() {
        Categoria categoria = new Categoria();
        categoria.setNome("Membros Superiores");
        categoriaRepository.save(categoria);

        Optional<Categoria> resultado = categoriaRepository.findFirstByNomeContainingIgnoreCase("superior");

        assertTrue(resultado.isPresent());
        assertEquals("Membros Superiores", resultado.get().getNome());
    }
}
