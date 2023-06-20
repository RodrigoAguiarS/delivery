package com.rodrigo.delivery.rest;

import com.rodrigo.delivery.domain.Venda;
import com.rodrigo.delivery.domain.dto.VendaDto;
import com.rodrigo.delivery.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Venda> realizarVenda(@RequestBody VendaDto vendaDto, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Venda venda = vendaService.realizarVenda(vendaDto.getClienteId(), vendaDto.getItensVenda(), emailUsuario);

        String uriString = "/vendas/" + venda.getId();
        URI uri = URI.create(uriString);

        return ResponseEntity.created(uri).body(venda);
    }
}
