package br.com.kevenaraujo.fisiofacil.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void findByEmailDeveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario();
        usuario.setEmail("repo@test.com");
        usuario.setSenha("hash");
        usuario.setNomeUsuario("repo-user");
        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByEmail("repo@test.com");

        assertTrue(encontrado.isPresent());
        assertEquals("repo-user", encontrado.get().getNomeUsuario());
    }

    @Test
    void findByResetTokenDeveRetornarUsuarioQuandoTokenExistir() {
        Usuario usuario = new Usuario();
        usuario.setEmail("token@test.com");
        usuario.setSenha("hash");
        usuario.setNomeUsuario("token-user");
        usuario.setResetToken("token-123");
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByResetToken("token-123");

        assertTrue(encontrado.isPresent());
        assertEquals("token@test.com", encontrado.get().getEmail());
    }
}
