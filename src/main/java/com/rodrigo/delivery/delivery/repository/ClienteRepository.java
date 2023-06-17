package com.rodrigo.delivery.delivery.repository;

import com.rodrigo.delivery.delivery.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}