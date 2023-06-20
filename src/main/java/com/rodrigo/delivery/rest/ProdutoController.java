package com.rodrigo.delivery.rest;

import com.rodrigo.delivery.domain.Produto;
import com.rodrigo.delivery.domain.dto.ProdutoDto;
import com.rodrigo.delivery.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoDto produtoDto) {
        Produto produto = produtoService.criarProduto(produtoDto);

        String uriString = "/produtos/" + produto.getId();
        URI uri = URI.create(uriString);

        return ResponseEntity.created(uri).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDto produtoDto) {
        Produto produtoAtualizado = produtoService.atualizarProduto(id, produtoDto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping
    public List<Produto> buscarTodosProdutos() {
        return produtoService.buscarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPproduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.ok("Produto deletado com sucesso");

    }
}
