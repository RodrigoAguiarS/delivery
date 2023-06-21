package com.rodrigo.delivery.service;

import com.rodrigo.delivery.domain.Cliente;
import com.rodrigo.delivery.domain.Endereco;
import com.rodrigo.delivery.domain.Perfil;
import com.rodrigo.delivery.domain.dto.ClienteDto;
import com.rodrigo.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.repository.ClienteRepository;
import com.rodrigo.delivery.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;
    private final BuscarEnderecoService buscarEnderecoService;
    private final PerfilRepository perfilRepository;


    /**
     * Cria um novo cliente com base nos dados fornecidos no objeto ClienteDto.
     * O método busca o endereço correspondente ao CEP fornecido e utiliza-o para preencher o endereço do cliente.
     *
     * @param clienteDTO O objeto ClienteDto contendo os dados do cliente a ser criado.
     * @return O cliente criado.
     */
    public Cliente criarCliente(ClienteDto clienteDTO) {

        String cep = clienteDTO.getEndereco().getCep();

        EnderecoDto enderecoCompleto = buscarEnderecoService.buscarEndereco(cep);

        Endereco endereco = modelMapper.map(enderecoCompleto, Endereco.class);
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        endereco.setNumeroCasa(cliente.getEndereco().getNumeroCasa());
        cliente.setEndereco(endereco);
        // Buscar o perfil de cliente do banco de dados
        Optional<Perfil> perfilClienteOptional = perfilRepository.findByNome("CLIENTE");
        Perfil perfilCliente = perfilClienteOptional.orElse(null);

        // Se o perfil de cliente não existir, criar um novo perfil de cliente
        if (perfilCliente == null) {
            perfilCliente = new Perfil();
            perfilCliente.setNome("CLIENTE");
            perfilCliente = perfilRepository.save(perfilCliente);
        }

        cliente.getPerfis().add(perfilCliente);

        return clienteRepository.save(cliente);
    }

    /**
     * Atualiza as informações de um cliente existente.
     *
     * @param id  O ID do cliente a ser atualizado
     * @param clienteDto  O DTO contendo os novos dados do cliente
     * @return O cliente atualizado
     * @throws ClienteNotFoundException Se o cliente com o ID fornecido não for encontrado
     */
    public Cliente atualizarCliente(Long id, ClienteDto clienteDto) {
        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setNome(clienteDto.getNome());
        clienteExistente.setTelefone(clienteDto.getTelefone());

        Endereco enderecoAtualizado = buscarEnderecoService.atualizarEndereco(
                clienteExistente.getEndereco().getId(), clienteDto.getEndereco().getCep());

        clienteExistente.setEndereco(enderecoAtualizado);

        return clienteRepository.save(clienteExistente);
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id o ID do cliente a ser buscado
     * @return o cliente encontrado
     * @throws NotFoundException se o cliente não for encontrado
     */
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
     * @param id ID do cliente a ser deletado.
     */
    public void deletarCliente(Long id) {

        Cliente cliente = buscarPorId(id);

        Endereco endereco = cliente.getEndereco();

        if (endereco != null) {
            buscarEnderecoService.deletarEndereco(endereco.getId());
        }
        clienteRepository.delete(cliente);
    }
}
