package br.com.kevenaraujo.fisiofacil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Usuario findByEmail( String email);

    Optional<Usuario> findByResetToken(String resetToken);

}
