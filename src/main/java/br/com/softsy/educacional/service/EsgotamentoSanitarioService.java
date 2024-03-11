package br.com.softsy.educacional.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softsy.educacional.dto.EsgotamentoSanitarioDTO;
import br.com.softsy.educacional.infra.exception.UniqueException;
import br.com.softsy.educacional.model.EsgotamentoSanitario;
import br.com.softsy.educacional.repository.EsgotamentoSanitarioRepository;

@Service
public class EsgotamentoSanitarioService {

	@Autowired
	private EsgotamentoSanitarioRepository repository;
	
	public List<EsgotamentoSanitario> listarTudo(){
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	public EsgotamentoSanitarioDTO buscarPorId(Long id){
		return new EsgotamentoSanitarioDTO(repository.getReferenceById(id));
	}
	
	@Transactional
	public EsgotamentoSanitarioDTO salvar(EsgotamentoSanitarioDTO dto) {
		validarEsgotamentoSanitario(dto.getEsgotamentoSanitario());
		
		EsgotamentoSanitario esgotamento = criarEsgotamentoAPartirDTO(dto);
		
		esgotamento = repository.save(esgotamento);
		return new EsgotamentoSanitarioDTO(esgotamento);
	}
	
	private EsgotamentoSanitario criarEsgotamentoAPartirDTO(EsgotamentoSanitarioDTO dto) {
		EsgotamentoSanitario esgotamento = new EsgotamentoSanitario();
		BeanUtils.copyProperties(dto, esgotamento, "idEsgotamentoSanitario", "ativo", "dataCadastro");
		esgotamento.setAtivo('S');
		esgotamento.setDataCadastro(LocalDateTime.now());
		return esgotamento;
	}
	
	@Transactional
	public EsgotamentoSanitarioDTO atualizar(EsgotamentoSanitarioDTO dto) {
		EsgotamentoSanitario esgotamento = repository.getReferenceById(dto.getIdEsgotamentoSanitario());
		atualizaDados(esgotamento, dto);
		return new EsgotamentoSanitarioDTO(esgotamento);
	}
	
	@Transactional
	public void ativaDesativa(char status, Long idEsgotamentoSanitario) {
		EsgotamentoSanitario esgotamento = repository.getReferenceById(idEsgotamentoSanitario);
		esgotamento.setAtivo(status);
	}
	
	private void validarEsgotamentoSanitario(String esgotamentoSanitario) {
		Optional<EsgotamentoSanitario> esgotamentoExistente = repository.findByEsgotamentoSanitario(esgotamentoSanitario).stream().findFirst();
		if(esgotamentoExistente.isPresent()) {
			throw new UniqueException("Esse esgotamento já existe.");
		}
	}
	
	private void atualizaDados(EsgotamentoSanitario destino, EsgotamentoSanitarioDTO origem) {
		BeanUtils.copyProperties(origem, destino, "idEsgotamentoSanitario", "ativo", "dataCadastro");
		
	}
}
