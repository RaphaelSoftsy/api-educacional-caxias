package br.com.softsy.educacional.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softsy.educacional.dto.AlunoDTO;
import br.com.softsy.educacional.dto.CadastroAlunoDTO;
import br.com.softsy.educacional.infra.config.PasswordEncrypt;
import br.com.softsy.educacional.model.Aluno;
import br.com.softsy.educacional.repository.AlunoRepository;
import br.com.softsy.educacional.repository.CandidatoRepository;
import br.com.softsy.educacional.repository.ContaRepository;
import br.com.softsy.educacional.repository.CursoRepository;
import br.com.softsy.educacional.repository.EscolaRepository;
import br.com.softsy.educacional.repository.PessoaRepository;
import br.com.softsy.educacional.repository.SerieRepository;
import br.com.softsy.educacional.repository.SituacaoAlunoRepository;
import br.com.softsy.educacional.repository.TurnoRepository;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private SituacaoAlunoRepository situacaoAlunoRepository;
    
	@Autowired
	private PasswordEncrypt encrypt;

    @Transactional(readOnly = true)
    public List<AlunoDTO> listarTudo() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(AlunoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoDTO buscarPorId(Long id) {
        return new AlunoDTO(alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado")));
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarPorIdConta(Long idConta) {
        List<Aluno> alunos = alunoRepository.findByConta_IdConta(idConta)
                .orElseThrow(() -> new IllegalArgumentException("Erro ao buscar aluno por ID da conta"));
        return alunos.stream()
                .map(AlunoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlunoDTO salvar(CadastroAlunoDTO dto) {
        Aluno aluno = criarAlunoAPartirDTO(dto);
        
        aluno.setSenha(encrypt.hashPassword(dto.getSenha()));
        aluno = alunoRepository.save(aluno);
        return new AlunoDTO(aluno);
    }

    @Transactional
    public AlunoDTO atualizar(CadastroAlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getIdAluno())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        atualizaDados(aluno, dto);
        aluno = alunoRepository.save(aluno);
        return new AlunoDTO(aluno);
    }

    @Transactional
    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }

    private Aluno criarAlunoAPartirDTO(CadastroAlunoDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setConta(contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada")));
        aluno.setCurso(cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado")));
        aluno.setEscola(escolaRepository.findById(dto.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada")));
        aluno.setSerie(serieRepository.findById(dto.getSerieId())
                .orElseThrow(() -> new IllegalArgumentException("Série não encontrada")));
        aluno.setTurno(turnoRepository.findById(dto.getTurnoId())
                .orElseThrow(() -> new IllegalArgumentException("Turno não encontrado")));
        aluno.setPessoa(pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada")));
        aluno.setCandidato(candidatoRepository.findById(dto.getCandidatoId())
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado")));
        aluno.setSituacaoAluno(situacaoAlunoRepository.findById(dto.getSituacaoAlunoId())
                .orElseThrow(() -> new IllegalArgumentException("Situação do Aluno não encontrada")));
        aluno.setDataCadastro(dto.getDataCadastro() != null ? dto.getDataCadastro() : LocalDateTime.now());
        aluno.setAluno(dto.getAluno());
        aluno.setEmailInterno(dto.getEmailInterno());
        aluno.setSenha(dto.getSenha());
        return aluno;
    }

    private void atualizaDados(Aluno aluno, CadastroAlunoDTO dto) {
        aluno.setConta(contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada")));
        aluno.setCurso(cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado")));
        aluno.setEscola(escolaRepository.findById(dto.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada")));
        aluno.setSerie(serieRepository.findById(dto.getSerieId())
                .orElseThrow(() -> new IllegalArgumentException("Série não encontrada")));
        aluno.setTurno(turnoRepository.findById(dto.getTurnoId())
                .orElseThrow(() -> new IllegalArgumentException("Turno não encontrado")));
        aluno.setPessoa(pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada")));
        aluno.setCandidato(candidatoRepository.findById(dto.getCandidatoId())
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado")));
        aluno.setSituacaoAluno(situacaoAlunoRepository.findById(dto.getSituacaoAlunoId())
                .orElseThrow(() -> new IllegalArgumentException("Situação do Aluno não encontrada")));
        
        BeanUtils.copyProperties(dto, aluno, "idUsuario", "ativo", "dataCadastro", "senha", "emailInterno");
        aluno.setAluno(dto.getAluno());

        if(dto.getSenha() != null) {
        	aluno.setSenha(encrypt.hashPassword(dto.getSenha()));
		}
    }
}