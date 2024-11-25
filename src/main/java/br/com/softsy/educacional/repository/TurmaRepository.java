package br.com.softsy.educacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.com.softsy.educacional.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long>{
	
	@Query("select turma from Turma turma join turma.escola escola where escola.idEscola = :idEscola")
    Optional<List<Turma>> findByEscola_IdEscola(@Param("idEscola") Long idEscola);

    @Procedure(name = "PROC_LISTA_TURMA_ESCOLA_DISCIPLINA")
    List<Object[]> filtrarTurmaPorDisciplinaEEscola(
            @Param("P_ID_ESCOLA") Long idEscola,
            @Param("P_ID_DISCIPLINA") Long idDisciplina
    );
    
    @Procedure(name = "PROC_LISTAR_TURMAS_SECRETARIA")
    List<Object[]> listarTurmasSecretaria();
    
    @Procedure(name = "PROC_LISTAR_ALUNOS_TURMA")
    List<Object[]> listarAlunosTurma(
        @Param("P_ID_TURMA") Long idTurma
    );

}
