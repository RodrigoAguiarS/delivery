package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.domain.Cliente;
import com.rodrigo.delivery.delivery.domain.Endereco;
import com.rodrigo.delivery.delivery.domain.dto.ClienteDto;
import com.rodrigo.delivery.delivery.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;
    private final BuscarEnderecoService buscarEnderecoService;


    /**
     * Cria um novo cliente com base nos dados fornecidos no objeto ClienteDto.
     * O método busca o endereço correspondente ao CEP fornecido e utiliza-o para preencher o endereço do cliente.
     *
     * @param clienteDTO O objeto ClienteDto contendo os dados do cliente a ser criado.
     * @return O cliente criado.
     */
    public Cliente criarCliente(ClienteDto clienteDTO) {

        String cep = clienteDTO.getEndereco().getCep();

        var enderecoCompleto = buscarEnderecoService.buscarEndereco(cep);

        Endereco endereco = modelMapper.map(enderecoCompleto, Endereco.class);

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEndereco(endereco);
        cliente.getEndereco().setNumeroCasa(clienteDTO.getEndereco().getNumeroCasa());
        cliente.setTelefone(clienteDTO.getTelefone());

        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long clienteId, ClienteDto clienteDto) {
        Cliente clienteExistente = buscarPorId(clienteId);

        clienteExistente.setNome(clienteDto.getNome());
        clienteExistente.setTelefone(clienteDto.getTelefone());

        Endereco enderecoAtualizado = buscarEnderecoService.atualizarEndereco(
                clienteExistente.getEndereco().getId(), clienteDto.getEndereco().getCep());

        clienteExistente.setEndereco(enderecoAtualizado);

        return clienteRepository.save(clienteExistente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    /**
     * Busca todos os clientes cadastrados.
     * @return Lista de clientes cadastrados.
     */
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Deleta um cliente e seu endereço associado.
     * @param clienteId ID do cliente a ser deletado.
     */
    public void deletarCliente(Long clienteId) {

        Cliente cliente = buscarPorId(clienteId);

        Endereco endereco = cliente.getEndereco();

        if (endereco != null) {
            buscarEnderecoService.deletarEndereco(endereco.getId());
        }
        clienteRepository.delete(cliente);
    }
}
