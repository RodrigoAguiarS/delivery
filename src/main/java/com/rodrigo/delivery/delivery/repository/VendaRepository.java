package com.rodrigo.delivery.delivery.repository;

import com.rodrigo.delivery.delivery.domain.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
