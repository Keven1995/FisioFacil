package br.com.kevenaraujo.fisiofacil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kevenaraujo.fisiofacil.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findFirstByNomeContainingIgnoreCase(String nome);
}

