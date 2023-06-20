package com.rodrigo.delivery.repository;

import com.rodrigo.delivery.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Perfil findByNome(String cliente);
}
