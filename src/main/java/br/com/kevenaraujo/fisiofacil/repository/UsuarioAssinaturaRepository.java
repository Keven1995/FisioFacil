package br.com.kevenaraujo.fisiofacil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kevenaraujo.fisiofacil.entity.UsuarioAssinatura;

public interface UsuarioAssinaturaRepository extends JpaRepository<UsuarioAssinatura, Long> {

    Optional<UsuarioAssinatura> findByUsuarioId(Long usuarioId);
}
