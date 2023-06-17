package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.config.AppConfig;
import com.rodrigo.delivery.delivery.domain.Endereco;
import com.rodrigo.delivery.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.delivery.domain.dto.ViaCepReposta;
import com.rodrigo.delivery.delivery.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class BuscarEnderecoService {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final EnderecoRepository enderecoRepository;
    private final ModelMapper modelMapper;

    /**
     * Busca o endereço correspondente ao CEP fornecido e retorna um objeto EnderecoDto preenchido.
     *
     * @param cep O CEP a ser pesquisado.
     * @return O objeto EnderecoDto preenchido com os dados do endereço correspondente ao CEP.
     */
    public EnderecoDto buscarEndereco(String cep) {

        String url = appConfig.getCepUrl() + cep + "/json/";
        ResponseEntity<ViaCepReposta> response = restTemplate.getForEntity(url, ViaCepReposta.class);
        ViaCepReposta viaCepReposta = response.getBody();

        EnderecoDto enderecoDto = new EnderecoDto();
        enderecoDto.setRua(viaCepReposta.getLogradouro());
        enderecoDto.setBairro(viaCepReposta.getBairro());
        enderecoDto.setCep(viaCepReposta.getCep());

        return enderecoDto;
    }
    public Endereco atualizarEndereco(Long enderecoId, String cep) {
        Endereco enderecoExistente = buscarEnderecoPorId(enderecoId);

        EnderecoDto enderecoAtualizado = buscarEndereco(cep);

        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoExistente;
    }
    public Endereco buscarEnderecoPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereção não encontrado"));
    }
    public void deletarEndereco(Long enderecoId) {
        enderecoRepository.deleteById(enderecoId);
    }
}
