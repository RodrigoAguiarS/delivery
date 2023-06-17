package com.rodrigo.delivery.delivery.repository;

import com.rodrigo.delivery.delivery.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
