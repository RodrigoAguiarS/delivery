package com.rodrigo.delivery.delivery.service;

import com.rodrigo.delivery.delivery.config.AppConfig;
import com.rodrigo.delivery.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.delivery.domain.dto.ViaCepReposta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BuscarEnderecoService {

    private final RestTemplate restTemplate;

    private final AppConfig appConfig;

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
}
