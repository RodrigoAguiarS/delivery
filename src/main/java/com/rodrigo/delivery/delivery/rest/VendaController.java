package com.rodrigo.delivery.delivery.rest;

import com.rodrigo.delivery.delivery.domain.Venda;
import com.rodrigo.delivery.delivery.domain.dto.VendaDto;
import com.rodrigo.delivery.delivery.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> realizarVenda(@RequestBody VendaDto vendaDto) {
        Venda venda = vendaService.realizarVenda(vendaDto.getClienteId(), vendaDto.getItensVenda());

        String uriString = "/vendas/" + venda.getId();
        URI uri = URI.create(uriString);

        return ResponseEntity.created(uri).body(venda);
    }
}
