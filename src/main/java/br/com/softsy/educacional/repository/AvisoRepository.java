package br.com.softsy.educacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.softsy.educacional.model.Aviso;
import br.com.softsy.educacional.model.TipoAviso;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    
    @Query("select aviso from Aviso aviso join aviso.professor professor where professor.idProfessor = :idProfessor")
    Optional<List<Aviso>> findByProfessor_IdProfessor(@Param("idProfessor") Long idProfessor);
    
    @Query("select aviso from Aviso join aviso.conta conta where conta.idConta = :idConta")
    Optional<List<Aviso>> findByConta_IdConta(@Param("idConta") Long idConta);
    
   
}


