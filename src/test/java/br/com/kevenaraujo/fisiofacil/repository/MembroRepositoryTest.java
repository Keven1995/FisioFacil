package br.com.kevenaraujo.fisiofacil.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.kevenaraujo.fisiofacil.entity.Categoria;
import br.com.kevenaraujo.fisiofacil.entity.Membro;

@DataJpaTest
class MembroRepositoryTest {

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void findByCategoriaIdDeveRetornarMembrosDaCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Membros Superiores");
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        Membro membro = new Membro();
        membro.setNome("Ombro");
        membro.setCategoria(categoriaSalva);
        membroRepository.save(membro);

        List<Membro> encontrados = membroRepository.findByCategoriaId(categoriaSalva.getId());

        assertEquals(1, encontrados.size());
        assertEquals("Ombro", encontrados.get(0).getNome());
    }
}
