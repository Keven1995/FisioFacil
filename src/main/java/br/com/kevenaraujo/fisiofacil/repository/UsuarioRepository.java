package br.com.kevenaraujo.fisiofacil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {


    Usuario findByEmail( String email);

    Optional<Usuario> findByResetToken(String resetToken);

    Usuario save(Usuario usuario);

    List<Usuario> findAll();

}
