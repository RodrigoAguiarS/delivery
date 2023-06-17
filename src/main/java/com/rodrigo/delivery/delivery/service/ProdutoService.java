package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.domain.Produto;
import com.rodrigo.delivery.delivery.domain.Endereco;
import com.rodrigo.delivery.delivery.domain.Produto;
import com.rodrigo.delivery.delivery.domain.dto.ProdutoDto;
import com.rodrigo.delivery.delivery.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ModelMapper modelMapper;

    /**
     * Cria um novo produto com base nos dados fornecidos no objeto ProdutoDto.
     *
     * @param produtoDto Dados do produto a ser criado
     * @return O produto criado e salvo no banco de dados
     */
    public Produto criarProduto(ProdutoDto produtoDto) {
        Produto produtoCriado = modelMapper.map(produtoDto, Produto.class);

        return produtoRepository.save(produtoCriado);
    }

    /**
     * Atualiza os dados de um produto existente com base no ID fornecido e nos dados do objeto ProdutoDto.
     *
     * @param id         ID do produto a ser atualizado
     * @param produtoDto Dados do produto atualizado
     * @return O produto atualizado e salvo no banco de dados
     */
    public Produto atualizarProduto(Long id, ProdutoDto produtoDto) {
        Produto produtoExistente = buscarPorId(id);
        modelMapper.map(produtoDto, produtoExistente);

        return produtoRepository.save(produtoExistente);
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id ID do produto a ser buscado
     * @return O produto encontrado
     * @throws NotFoundException se o produto não for encontrado
     */
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    /**
     * Busca todos os produtos cadastrados.
     *
     * @return Lista de todos os produtos cadastrados
     */
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * Deleta um produto pelo seu ID.
     *
     * @param id ID do produto a ser deletado
     */
    public void deletarProduto(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}
