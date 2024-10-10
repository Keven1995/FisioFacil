package br.com.kevenaraujo.fisiofacil.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kevenaraujo.fisiofacil.entity.Membro;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    List<Membro> findByCategoriaId(Long categoriaId);
}

