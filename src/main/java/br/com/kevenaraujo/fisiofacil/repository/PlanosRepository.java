package br.com.kevenaraujo.fisiofacil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kevenaraujo.fisiofacil.entity.Planos;

@Repository
public interface PlanosRepository extends JpaRepository<Planos, Long> {
}

