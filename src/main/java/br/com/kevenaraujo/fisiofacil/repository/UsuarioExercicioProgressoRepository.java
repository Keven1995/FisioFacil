package br.com.kevenaraujo.fisiofacil.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kevenaraujo.fisiofacil.entity.UsuarioExercicioProgresso;

public interface UsuarioExercicioProgressoRepository extends JpaRepository<UsuarioExercicioProgresso, Long> {

    List<UsuarioExercicioProgresso> findByUsuarioIdAndExercicioKeyIn(Long usuarioId, Collection<String> exercicioKeys);

    Optional<UsuarioExercicioProgresso> findByUsuarioIdAndExercicioKey(Long usuarioId, String exercicioKey);
}
