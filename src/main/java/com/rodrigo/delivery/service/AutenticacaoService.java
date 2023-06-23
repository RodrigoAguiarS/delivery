package com.rodrigo.delivery.service;

import com.rodrigo.delivery.domain.Endereco;
import com.rodrigo.delivery.domain.Perfil;
import com.rodrigo.delivery.domain.Pessoa;
import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.domain.dto.PerfilDto;
import com.rodrigo.delivery.domain.dto.UsuarioDto;
import com.rodrigo.delivery.error.Mensagens;
import com.rodrigo.delivery.error.MensagensErroException;
import com.rodrigo.delivery.repository.PerfilRepository;
import com.rodrigo.delivery.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe responsável por fornecer serviços de autenticação e gerenciamento de usuários.
 */
@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final BuscarEnderecoService buscarEnderecoService;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;

    /**
     * Carrega os detalhes do usuário com o nome de usuário fornecido.
     *
     * @param username O nome de usuário a ser carregado.
     * @return Os detalhes do usuário correspondentes ao nome de usuário fornecido.
     * @throws UsernameNotFoundException Se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new UsernameNotFoundException("Dados inválidos!");
    }

    /**
     * Cria um novo usuário com base nos dados fornecidos no objeto UsuarioDto.
     *
     * @param usuarioDto O objeto UsuarioDto contendo os dados do usuário a ser criado.
     * @return O usuário criado.
     */
    public Usuario criarUsuario(UsuarioDto usuarioDto) {
        Mensagens mensagens = new Mensagens();

        String email = usuarioDto.getEmail();
        String emailLowerCase = email.toLowerCase();

        validarEmailExistente(emailLowerCase, mensagens);

        if (mensagens.possuiErros()) {
            throw new MensagensErroException(mensagens);
        }

        Usuario usuario = criarUsuarioComDados(usuarioDto, emailLowerCase);
        return usuarioRepository.save(usuario);
    }

    private void validarEmailExistente(String email, Mensagens mensagens) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmailIgnoreCase(email);

        if (usuarioExistente.isPresent()) {
            mensagens.adicionarErro("Email já existe na base de dados");
        }
    }

    private Usuario criarUsuarioComDados(UsuarioDto usuarioDto, String email) {
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
        usuario.setEmail(email);

        String cep = usuarioDto.getEndereco().getCep();
        EnderecoDto enderecoCompleto = buscarEnderecoService.buscarEndereco(cep);
        Endereco endereco = modelMapper.map(enderecoCompleto, Endereco.class);
        endereco.setNumeroCasa(usuarioDto.getEndereco().getNumeroCasa());
        usuario.setEndereco(endereco);

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.getSenha());
        usuario.setSenha(senhaCriptografada);

        Optional<Perfil> perfilPadraoOptional = perfilRepository.findByNome("USUARIO");
        Perfil perfilPadrao = perfilPadraoOptional.orElseGet(() -> {
            Perfil novoPerfil = new Perfil();
            novoPerfil.setNome("USUARIO");
            return perfilRepository.save(novoPerfil);
        });
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilPadrao);
        usuario.setPerfis(perfis);

        return usuario;
    }

    /**
     * Verifica se um perfil corresponde ao perfil de administração.
     *
     * @param authority O nome do perfil a ser verificado.
     * @return true se o perfil for de administração, false caso contrário.
     */
    public boolean isPerfilAdmin(String authority) {
        // Verifique se o perfil é de administração
        Optional<Perfil> perfilOptional = perfilRepository.findByNome(authority);
        return perfilOptional.isPresent() && perfilOptional.get().getNome().equals("ADMIN");
    }
}
