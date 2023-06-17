package com.rodrigo.delivery.delivery.rest;

import com.rodrigo.delivery.delivery.domain.Cliente;
import com.rodrigo.delivery.delivery.domain.dto.ClienteDto;
import com.rodrigo.delivery.delivery.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long clienteId, @RequestBody ClienteDto clienteDto) {
        Cliente clienteAtualizado = clienteService.atualizarCliente(clienteId, clienteDto);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }
    @GetMapping
    public List<Cliente> buscarTodosClientes() {
        return clienteService.buscarTodos();
    }
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long clienteId) {
        clienteService.deletarCliente(clienteId);
        return ResponseEntity.ok("Cliente deletado com sucesso");
    }
}