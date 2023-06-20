package com.rodrigo.delivery.service;

import com.rodrigo.delivery.domain.Endereco;
import com.rodrigo.delivery.domain.Perfil;
import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.domain.dto.PerfilDto;
import com.rodrigo.delivery.domain.dto.UsuarioDto;
import com.rodrigo.delivery.repository.PerfilRepository;
import com.rodrigo.delivery.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final BuscarEnderecoService buscarEnderecoService;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new UsernameNotFoundException("Dados inválidos!");
    }
    public Usuario criarUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);

        String cep = usuarioDto.getEndereco().getCep();
        EnderecoDto enderecoCompleto = buscarEnderecoService.buscarEndereco(cep);
        Endereco endereco = modelMapper.map(enderecoCompleto, Endereco.class);
        endereco.setNumeroCasa(usuarioDto.getEndereco().getNumeroCasa());
        usuario.setEndereco(endereco);

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.getSenha());
        usuario.setSenha(senhaCriptografada);

        List<PerfilDto> perfisDto = usuarioDto.getPerfis();
        List<Perfil> perfis = new ArrayList<>();
        for (PerfilDto perfilDto : perfisDto) {
            ;          Perfil perfil = modelMapper.map(perfilDto, Perfil.class);
            perfis.add(perfil);
        }
        usuario.setPerfis(perfis);

        // Salvar os perfis antes de salvar o usuário
        for (Perfil perfil : perfis) {
            perfilRepository.save(perfil);
        }

        return usuarioRepository.save(usuario);
    }
}
