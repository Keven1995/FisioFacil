package br.com.kevenaraujo.fisiofacil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar um usuário pelo email

    Usuario findByEmail( String email);

}
