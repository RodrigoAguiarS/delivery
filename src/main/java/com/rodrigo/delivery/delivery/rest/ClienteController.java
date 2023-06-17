package com.rodrigo.delivery.delivery.rest;

import com.rodrigo.delivery.delivery.domain.Cliente;
import com.rodrigo.delivery.delivery.domain.dto.ClienteDto;
import com.rodrigo.delivery.delivery.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = clienteService.criarCliente(clienteDto);

        String uriString = "/clientes/" + cliente.getId();
        URI uri = URI.create(uriString);

        return ResponseEntity.created(uri).body(cliente);
    }

}
