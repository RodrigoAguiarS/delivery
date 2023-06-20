package com.rodrigo.delivery.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class VendaDto {
    private Long clienteId;
    private List<ItemVendaDto> itensVenda;

}
