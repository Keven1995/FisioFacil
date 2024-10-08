package br.com.kevenaraujo.fisiofacil.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;

@Repository
public interface MembroPlanoRepository extends JpaRepository<MembroPlano, Long> {
    List<MembroPlano> findByMembroId(Long membroId);
}

