package br.com.softsy.educacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.com.softsy.educacional.model.AtoRegulatorio;
import br.com.softsy.educacional.model.Zoneamento;

public interface AtoRegulatorioRepository extends JpaRepository<AtoRegulatorio, Long>{

	List<AtoRegulatorio> findByAtoRegulatorio(String atoRegulatorio);
	
	@Query("select atoRegulatorio from AtoRegulatorio join atoRegulatorio.conta conta where conta.idConta = :idConta")
    Optional<List<AtoRegulatorio>> findByConta_IdConta(@Param("idConta") Long idConta);
}
