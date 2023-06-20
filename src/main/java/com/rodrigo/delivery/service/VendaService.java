package com.rodrigo.delivery.service;

import com.rodrigo.delivery.domain.*;
import com.rodrigo.delivery.domain.dto.ItemVendaDto;
import com.rodrigo.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
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

    private final UsuarioRepository usuarioRepository;


    public Venda realizarVenda(Long clienteId, List<ItemVendaDto> itensVenda, String email) {
        // Verifica se o cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));

        // Mapeia os itens de venda para a entidade ItemVenda
        List<ItemVenda> itens = mapearItensVenda(itensVenda);

        // Calcula o valor total da venda
        BigDecimal valorTotal = calcularValorTotal(itens);

        Date dataAtual = new Date();

        // Cria a entidade de venda
        Venda venda = new Venda();
        venda.setUsuario(usuario);
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

