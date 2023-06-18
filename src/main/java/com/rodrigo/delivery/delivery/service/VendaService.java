package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.domain.Cliente;
import com.rodrigo.delivery.delivery.domain.ItemVenda;
import com.rodrigo.delivery.delivery.domain.Produto;
import com.rodrigo.delivery.delivery.domain.Venda;
import com.rodrigo.delivery.delivery.domain.dto.ItemVendaDto;
import com.rodrigo.delivery.delivery.repository.ClienteRepository;
import com.rodrigo.delivery.delivery.repository.ItemVendaRepository;
import com.rodrigo.delivery.delivery.repository.ProdutoRepository;
import com.rodrigo.delivery.delivery.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;

    public Venda realizarVenda(Long clienteId, List<ItemVendaDto> itensVenda) {
        // Verifica se o cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        // Mapeia os itens de venda para a entidade ItemVenda
        List<ItemVenda> itens = mapearItensVenda(itensVenda);

        // Calcula o valor total da venda
        BigDecimal valorTotal = calcularValorTotal(itens);

        Date dataAtual = new Date();

        // Cria a entidade de venda
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setData(dataAtual);
        venda.setValorTotal(valorTotal);

        // Salva a venda no banco de dados
        Venda vendaSalva = vendaRepository.save(venda);
        // Associa a venda aos itens de venda e salva os itens no banco de dados
        for (ItemVenda item : itens) {
            item.setVenda(vendaSalva);
            itemVendaRepository.save(item);
        }

        return vendaSalva;
    }

    private List<ItemVenda> mapearItensVenda(List<ItemVendaDto> itensVenda) {
        return itensVenda.stream()
                .map(this::mapearItemVenda)
                .collect(Collectors.toList());
    }

    private ItemVenda mapearItemVenda(ItemVendaDto itemVendaDto) {
        Produto produto = produtoRepository.findById(itemVendaDto.getIdProduto())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setProduto(produto);
        itemVenda.setQuantidade(itemVendaDto.getQuantidade());
        itemVenda.setPrecoUnitario(produto.getPreco());

        return itemVenda;
    }

    private BigDecimal calcularValorTotal(List<ItemVenda> itensVenda) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemVenda itemVenda : itensVenda) {
            BigDecimal valorItem = itemVenda.getPrecoUnitario().multiply(BigDecimal.valueOf(itemVenda.getQuantidade()));
            valorTotal = valorTotal.add(valorItem);
        }
        return valorTotal;
    }
}

