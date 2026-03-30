package br.com.kevenaraujo.fisiofacil.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.kevenaraujo.fisiofacil.entity.Categoria;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.entity.Planos;

@DataJpaTest
class MembroPlanoRepositoryTest {

    @Autowired
    private MembroPlanoRepository membroPlanoRepository;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private PlanosRepository planosRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void findByMembroIdDeveRetornarRelacoesDoMembro() {
        Categoria categoria = new Categoria();
        categoria.setNome("Membros Superiores");
        categoria = categoriaRepository.save(categoria);

        Membro membro = new Membro();
        membro.setNome("Ombro");
        membro.setCategoria(categoria);
        membro = membroRepository.save(membro);

        Planos plano = new Planos();
        plano.setNome("Plus");
        plano.setDescricao("Plano completo");
        plano.setPreco(89.9);
        plano = planosRepository.save(plano);

        MembroPlano relacao = new MembroPlano();
        relacao.setMembro(membro);
        relacao.setPlano(plano);
        membroPlanoRepository.save(relacao);

        List<MembroPlano> resultado = membroPlanoRepository.findByMembroId(membro.getId());

        assertEquals(1, resultado.size());
        assertEquals("Plus", resultado.get(0).getPlano().getNome());
    }

    @Test
    void findByPlanoIdDeveRetornarRelacoesDoPlano() {
        Categoria categoria = new Categoria();
        categoria.setNome("Membros Inferiores");
        categoria = categoriaRepository.save(categoria);

        Membro membro = new Membro();
        membro.setNome("Joelho");
        membro.setCategoria(categoria);
        membro = membroRepository.save(membro);

        Planos plano = new Planos();
        plano.setNome("Basico");
        plano.setDescricao("Plano inicial");
        plano.setPreco(49.9);
        plano = planosRepository.save(plano);

        MembroPlano relacao = new MembroPlano();
        relacao.setMembro(membro);
        relacao.setPlano(plano);
        membroPlanoRepository.save(relacao);

        List<MembroPlano> resultado = membroPlanoRepository.findByPlanoId(plano.getId());

        assertEquals(1, resultado.size());
        assertEquals("Joelho", resultado.get(0).getMembro().getNome());
    }
}
