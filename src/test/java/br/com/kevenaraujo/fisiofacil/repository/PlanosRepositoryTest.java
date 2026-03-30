package br.com.kevenaraujo.fisiofacil.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.kevenaraujo.fisiofacil.entity.Planos;

@DataJpaTest
class PlanosRepositoryTest {

    @Autowired
    private PlanosRepository planosRepository;

    @Test
    void findByNomeDeveRetornarPlanoQuandoExistir() {
        Planos plano = new Planos();
        plano.setNome("PlanoUnitarioTeste");
        plano.setDescricao("Plano completo");
        plano.setPreco(89.9);
        planosRepository.save(plano);

        Planos encontrado = planosRepository.findByNome("PlanoUnitarioTeste");

        assertNotNull(encontrado);
        assertEquals("PlanoUnitarioTeste", encontrado.getNome());
    }

    @Test
    void findByNomeDeveRetornarNullQuandoNaoExistir() {
        Planos encontrado = planosRepository.findByNome("Inexistente");

        assertNull(encontrado);
    }
}
