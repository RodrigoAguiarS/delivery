package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.domain.Cliente;
import com.rodrigo.delivery.delivery.domain.Endereco;
import com.rodrigo.delivery.delivery.domain.dto.ClienteDto;
import com.rodrigo.delivery.delivery.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
