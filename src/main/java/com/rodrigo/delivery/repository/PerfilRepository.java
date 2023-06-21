package com.rodrigo.delivery.repository;

import com.rodrigo.delivery.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNome(String nome);

    boolean existsByNome(String nome);
}
