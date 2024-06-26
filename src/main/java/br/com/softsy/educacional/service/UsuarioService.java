package br.com.softsy.educacional.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softsy.educacional.dto.UsuarioDTO;
import br.com.softsy.educacional.infra.config.PasswordEncrypt;
import br.com.softsy.educacional.infra.exception.UniqueException;
import br.com.softsy.educacional.model.Usuario;
import br.com.softsy.educacional.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
	@Autowired
	private PasswordEncrypt encrypt;

    public List<Usuario> listarTudo() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        return new UsuarioDTO(repository.getReferenceById(id));
    }

    @Transactional
    public UsuarioDTO salvar(UsuarioDTO dto) {
    	
        validarUsuario(dto.getUsuario());

        Usuario usuario = criarUsuarioAPartirDTO(dto);
        usuario.setSenha(encrypt.hashPassword(dto.getSenha()));
        usuario = repository.save(usuario);
        return new UsuarioDTO(usuario);
    }

    private Usuario criarUsuarioAPartirDTO(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(dto, usuario, "idUsuario", "ativo", "dataCadastro");
        usuario.setAtivo('S');
        usuario.setDataCadastro(LocalDateTime.now());
        return usuario;
    }

    @Transactional
    public UsuarioDTO atualizar(UsuarioDTO dto) {
        Usuario usuario = repository.getReferenceById(dto.getIdUsuario());
        atualizaDados(usuario, dto);
        return new UsuarioDTO(usuario);
    }

    @Transactional
    public void ativaDesativa(char status, Long idUsuario) {
        Usuario usuario = repository.getReferenceById(idUsuario);
        usuario.setAtivo(status);
    }

    private void validarUsuario(String usuario) {
        Optional<Usuario> usuarioExistente = repository.findByUsuario(usuario).stream().findFirst();
        if (usuarioExistente.isPresent()) {
            throw new UniqueException("Esse usuário já existe.");
        }
    }

    private void atualizaDados(Usuario destino, UsuarioDTO origem) {
        BeanUtils.copyProperties(origem, destino, "idUsuario", "ativo", "dataCadastro");
    }
}