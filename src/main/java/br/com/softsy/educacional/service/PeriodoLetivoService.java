package br.com.softsy.educacional.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softsy.educacional.dto.AreaConhecimentoDTO;
import br.com.softsy.educacional.dto.CadastroPeriodoLetivoDTO;
import br.com.softsy.educacional.dto.CargoProfessorDTO;
import br.com.softsy.educacional.dto.PeriodoLetivoDTO;
import br.com.softsy.educacional.infra.exception.UniqueException;
import br.com.softsy.educacional.model.AreaConhecimento;
import br.com.softsy.educacional.model.Conta;
import br.com.softsy.educacional.model.DependenciaAdministrativa;
import br.com.softsy.educacional.model.Disciplina;
import br.com.softsy.educacional.model.PeriodoLetivo;
import br.com.softsy.educacional.repository.ContaRepository;
import br.com.softsy.educacional.repository.DependenciaAdministrativaRepository;
import br.com.softsy.educacional.repository.PeriodoLetivoRepository;

@Service
public class PeriodoLetivoService {

    @Autowired
    private PeriodoLetivoRepository periodoLetivoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Transactional(readOnly = true)
    public List<PeriodoLetivoDTO> listarTudo() {
        List<PeriodoLetivo> periodosLetivos = periodoLetivoRepository.findAll();
        return periodosLetivos.stream()
                .map(PeriodoLetivoDTO::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PeriodoLetivoDTO buscarPorId(Long id) {
        return new PeriodoLetivoDTO(periodoLetivoRepository.getReferenceById(id));
    }
    
    
    @Transactional(readOnly = true)
    public List<PeriodoLetivoDTO> buscarPorIdConta(Long idConta) {
        List<PeriodoLetivo> areaConhecimento = periodoLetivoRepository.findByConta_IdConta(idConta)
                .orElseThrow(() -> new IllegalArgumentException("Erro ao buscar periodo letivo por ID da conta"));
        return areaConhecimento.stream()
                .map(PeriodoLetivoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PeriodoLetivoDTO salvar(CadastroPeriodoLetivoDTO dto) {
    	validarDescricao(dto.getDescricao());

        PeriodoLetivo periodoLetivo = criarPeriodoLetivoAPartirDTO(dto);
        periodoLetivo = periodoLetivoRepository.save(periodoLetivo);
        return new PeriodoLetivoDTO(periodoLetivo);
    }

    @Transactional
    public PeriodoLetivoDTO atualizar(CadastroPeriodoLetivoDTO dto) {
        PeriodoLetivo periodoLetivo = periodoLetivoRepository.findById(dto.getIdPeriodoLetivo())
                .orElseThrow(() -> new IllegalArgumentException("Período letivo não encontrado"));
        atualizarDados(periodoLetivo, dto);
        return new PeriodoLetivoDTO(periodoLetivo);
    }

    private PeriodoLetivo criarPeriodoLetivoAPartirDTO(CadastroPeriodoLetivoDTO dto) {
        PeriodoLetivo periodoLetivo = new PeriodoLetivo();
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        periodoLetivo.setConta(conta);
        periodoLetivo.setAno(dto.getAno());
        periodoLetivo.setPeriodo(dto.getPeriodo());
        periodoLetivo.setDtInicio(dto.getDtInicio());
        periodoLetivo.setDtFim(dto.getDtFim());
        periodoLetivo.setDescricao(dto.getDescricao());
        periodoLetivo.setTipoPeriodicidade(dto.getTipoPeriodicidade());
        periodoLetivo.setDataCadastro(LocalDateTime.now());
        periodoLetivo.setAtivo('S');
        return periodoLetivo;
    }

    private void validarDescricao(String descricao) {
        Optional<PeriodoLetivo> descricaoExistente = periodoLetivoRepository.findByDescricao(descricao).stream().findFirst();
        if (descricaoExistente.isPresent()) {
            throw new UniqueException("Essa descrição já existe.");
        }
    }
    
	@Transactional
	public void ativaDesativa(char status, Long idPeriodoLetivo) {
		PeriodoLetivo periodoLetivo = periodoLetivoRepository.getReferenceById(idPeriodoLetivo);
		periodoLetivo.setAtivo(status);
	}

    private void atualizarDados(PeriodoLetivo destino, CadastroPeriodoLetivoDTO origem) {
        Conta conta = contaRepository.findById(origem.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        destino.setConta(conta);
        destino.setAno(origem.getAno());
        destino.setPeriodo(origem.getPeriodo());
        destino.setDtInicio(origem.getDtInicio());
        destino.setDtFim(origem.getDtFim());
        destino.setDescricao(origem.getDescricao());
        destino.setTipoPeriodicidade(origem.getTipoPeriodicidade());
    }
}