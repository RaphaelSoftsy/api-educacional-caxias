package br.com.softsy.educacional.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softsy.educacional.dto.CadastroTurmaDTO;
import br.com.softsy.educacional.dto.ModalidadeEscolaDTO;
import br.com.softsy.educacional.dto.TurmaDTO;
import br.com.softsy.educacional.model.AnoEscolar;
import br.com.softsy.educacional.model.Escola;
import br.com.softsy.educacional.model.FormaOrganEnsino;
import br.com.softsy.educacional.model.GradeCurricular;
import br.com.softsy.educacional.model.ModalidadeEscola;
import br.com.softsy.educacional.model.PeriodoLetivo;
import br.com.softsy.educacional.model.TipoAtendimento;
import br.com.softsy.educacional.model.TipoDeMedicao;
import br.com.softsy.educacional.model.Turma;
import br.com.softsy.educacional.model.Turno;
import br.com.softsy.educacional.repository.AnoEscolarRepository;
import br.com.softsy.educacional.repository.EscolaRepository;
import br.com.softsy.educacional.repository.FormaOrganEnsinoRepository;
import br.com.softsy.educacional.repository.GradeCurricularRepository;
import br.com.softsy.educacional.repository.ModalidadeEscolaRepository;
import br.com.softsy.educacional.repository.PeriodoLetivoRepository;
import br.com.softsy.educacional.repository.TipoAtendimentoRepository;
import br.com.softsy.educacional.repository.TipoDeMedicaoRepository;
import br.com.softsy.educacional.repository.TurmaRepository;
import br.com.softsy.educacional.repository.TurnoRepository;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private PeriodoLetivoRepository periodoLetivoRepository;
    
    @Autowired
    private GradeCurricularRepository gradeCurricularRepository;
    
    @Autowired
    private EntityManager entityManager;


    @Transactional(readOnly = true)
    public List<TurmaDTO> listarTudo() {
        List<Turma> turmas = turmaRepository.findAll();
        return turmas.stream()
                .map(TurmaDTO::new)
                .collect(Collectors.toList());
    }
    
	@Transactional(readOnly = true)
	public TurmaDTO buscarPorId(Long id){
		return new TurmaDTO(turmaRepository.getReferenceById(id));
	}


    @Transactional
    public TurmaDTO salvar(CadastroTurmaDTO dto) {
        Turma turma = criarTurmaAPartirDTO(dto);
        turma = turmaRepository.save(turma);
        return new TurmaDTO(turma);
    }

    @Transactional
    public TurmaDTO atualizar(CadastroTurmaDTO dto) {
        Turma turma = turmaRepository.getReferenceById(dto.getIdTurma());
        atualizaDados(turma, dto);
        return new TurmaDTO(turma);
    }

    @Transactional
    public void remover(Long id) {
        turmaRepository.deleteById(id);
    }

    private Turma criarTurmaAPartirDTO(CadastroTurmaDTO dto) {
        Turma turma = new Turma();
        Escola escola = escolaRepository.findById(dto.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada"));
        Turno turno = turnoRepository.findById(dto.getTurnoId())
                .orElseThrow(() -> new IllegalArgumentException("Turno não encontrado"));
        
        PeriodoLetivo periodoLetivo = periodoLetivoRepository.findById(dto.getPeriodoLetivoId())
                .orElseThrow(() -> new IllegalArgumentException("Período letivo não encontrado"));
        GradeCurricular gradeCurricular = gradeCurricularRepository.findById(dto.getGradeCurricularId())
                .orElseThrow(() -> new IllegalArgumentException("Grade curricular não encontrada"));

        turma.setEscola(escola);
        turma.setPeriodoLetivo(periodoLetivo);
        turma.setGradeCurricular(gradeCurricular);
        turma.setNomeTurma(dto.getNomeTurma());
        turma.setCodTurmaInep(dto.getCodTurmaInep());
        turma.setTurno(turno);
        turma.setLibras(dto.getLibras());
        turma.setDataCadastro(LocalDateTime.now());
        turma.setAtivo('S');
        turma.setVagas(dto.getVagas());
        turma.setControlaVagas(dto.getControlaVagas());
        return turma;
    }

    private void atualizaDados(Turma destino, CadastroTurmaDTO origem) {
        Escola escola = escolaRepository.findById(origem.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada"));
      
        Turno turno = turnoRepository.findById(origem.getTurnoId())
                .orElseThrow(() -> new IllegalArgumentException("Turno não encontrado"));

        PeriodoLetivo periodoLetivo = periodoLetivoRepository.findById(origem.getPeriodoLetivoId())
                .orElseThrow(() -> new IllegalArgumentException("Período letivo não encontrado"));
        GradeCurricular gradeCurricular = gradeCurricularRepository.findById(origem.getGradeCurricularId())
                .orElseThrow(() -> new IllegalArgumentException("Grade curricular não encontrada"));

        destino.setEscola(escola);
        destino.setPeriodoLetivo(periodoLetivo);
        destino.setGradeCurricular(gradeCurricular);
        destino.setNomeTurma(origem.getNomeTurma());
        destino.setCodTurmaInep(origem.getCodTurmaInep());
        destino.setTurno(turno);
        destino.setLibras(origem.getLibras());
        destino.setDataCadastro(LocalDateTime.now());
        destino.setAtivo('S');
        destino.setVagas(origem.getVagas());
        destino.setControlaVagas(origem.getControlaVagas());
    }
    
    @Transactional
    public void ativaDesativa(char status, Long idTurma) {
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        turma.setAtivo(status);
    }
    
    
    public List<Map<String, Object>> filtrarTurmaPorEscolaEDisciplina(Long idEscola, Long idDisciplina) {
        StringBuilder sql = new StringBuilder();
        sql.append("CALL PROC_LISTA_TURMA_ESCOLA_DISCIPLINA(:pIdEscola, :pIdDisciplina)");

        Query query = entityManager.createNativeQuery(sql.toString());

        // Definir os parâmetros
        query.setParameter("pIdEscola", idEscola);
        query.setParameter("pIdDisciplina", idDisciplina);

        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> mappedResultList = new ArrayList<>();

        // Mapear os resultados para um formato de mapa
        for (Object[] result : resultList) {
        	Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("idTurma", result[0]);
            resultMap.put("idEscola", result[1]);
            resultMap.put("idPeriodoLetivo", result[2]);
            resultMap.put("idTurno", result[3]);
            resultMap.put("nomeTurma", result[4]);
            resultMap.put("codTurmaInep", result[5]);
            resultMap.put("idGradeCurricular", result[6]);
            resultMap.put("libras", result[7]);
            resultMap.put("dataCadastro", result[8]);
            resultMap.put("ativo", result[9]);
            resultMap.put("vagas", result[10]);
            resultMap.put("controlaVagas", result[11]);
            mappedResultList.add(resultMap);
        }

        return mappedResultList;
    }
    
    public List<Map<String, Object>> listarAlunosTurma(Long idTurma) {
        StringBuilder sql = new StringBuilder();
        sql.append("CALL PROC_LISTAR_ALUNOS_TURMA(:pIdTurma)");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("pIdTurma", idTurma);

        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> mappedResultList = new ArrayList<>();

        for (Object[] result : resultList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("idAluno", result[0]);
            resultMap.put("idConta", result[1]);
            resultMap.put("idCurso", result[2]);
            resultMap.put("idEscola", result[3]);
            resultMap.put("idSerie", result[4]);
            resultMap.put("idTurno", result[5]);
            resultMap.put("idPessoa", result[6]);
            resultMap.put("idCandidato", result[7]);
            resultMap.put("idSituacaoAluno", result[8]);
            resultMap.put("dataCadastro", result[9]);
            resultMap.put("aluno", result[10]);
            resultMap.put("emailInterno", result[11]);
            resultMap.put("senha", result[12]);
            resultMap.put("nomeCompleto", result[13]);
            resultMap.put("nomeCurso", result[14]);
            mappedResultList.add(resultMap);
        }

        return mappedResultList;
    }
    
    public List<Map<String, Object>> listarTurmasSecretaria() {
        StringBuilder sql = new StringBuilder();
        sql.append("CALL PROC_LISTAR_TURMAS_SECRETARIA()");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> mappedResultList = new ArrayList<>();

        for (Object[] result : resultList) {
        	Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("idTurma", result[0]);
            resultMap.put("nomeTurma", result[1]);
            resultMap.put("idDisciplina", result[2]);
            resultMap.put("codDisciplina", result[3]);
            resultMap.put("nome", result[4]);
            resultMap.put("ano", result[5]);
            resultMap.put("periodo", result[6]);
            resultMap.put("tipoPeriodicidade", result[7]);
            resultMap.put("aulasPrevistas", result[8]);
            resultMap.put("aulasDadas", result[9]);
            resultMap.put("prematriculas", result[10]);
            resultMap.put("matriculas", result[11]);
            resultMap.put("idProfessor", result[11]);
            mappedResultList.add(resultMap);
        }

        return mappedResultList;
    }
    
    
    public List<Map<String, Object>> filtrarTurmaDisciplinas(Long idProfessor, Integer ano, Long idPeriodoLetivo,
    		Long idEscola, Long idDisciplina, Long idTurno, Long idCurso) {
        StringBuilder sql = new StringBuilder();
        sql.append("CALL PROC_FILTRAR_TURMA_DISCIPLINAS(:pIdProfessor, :pAno, :pIdPeriodoLetivo, :pIdEscola, :pIdDisciplina, :pIdTurno, :pIdCurso)");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("pIdProfessor", idProfessor);
        query.setParameter("pAno", ano);
        query.setParameter("pIdPeriodoLetivo", idPeriodoLetivo);
        query.setParameter("pIdEscola", idEscola);
        query.setParameter("pIdDisciplina", idDisciplina);
        query.setParameter("pIdTurno", idTurno);
        query.setParameter("pIdCurso", idCurso);
     

        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> mappedResultList = new ArrayList<>();

        for (Object[] result : resultList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("idTurma", result[0]);
            resultMap.put("nomeTurma", result[1]);
            resultMap.put("idCurso", result[2]);
            resultMap.put("nomeCurso", result[3]);
            resultMap.put("idDisciplina", result[4]);
            resultMap.put("codDisciplina", result[5]);
            resultMap.put("nomeDisciplina", result[6]);
            resultMap.put("ano", result[7]);
            resultMap.put("periodo", result[8]);
            resultMap.put("tipoPeriodicidade", result[9]);
            resultMap.put("descricao", result[10]);
            resultMap.put("dataInicio", result[11]);
            resultMap.put("dataFim", result[12]);
            resultMap.put("idEscola", result[13]);
            resultMap.put("nomeEscola", result[14]);
            resultMap.put("mnemonico", result[15]);
            resultMap.put("turno", result[16]);
            mappedResultList.add(resultMap);
        }

        return mappedResultList;
    }
    
}