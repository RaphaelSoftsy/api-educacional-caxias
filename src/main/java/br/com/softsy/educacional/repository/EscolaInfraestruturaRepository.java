package br.com.softsy.educacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.softsy.educacional.model.EscolaInfraestrutura;

@Repository
public interface EscolaInfraestruturaRepository extends JpaRepository<EscolaInfraestrutura, Long>{
	
	@Query("select escolaInfraestrutura from EscolaInfraestrutura escolaInfraestrutura join escolaInfraestrutura.escola escola where escola.idEscola = :idEscola")
    Optional<List<EscolaInfraestrutura>> findByEscola_IdEscola(@Param("idEscola") Long idEscola);

}
