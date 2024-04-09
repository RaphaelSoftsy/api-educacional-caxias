package br.com.softsy.educacional.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "TBL_DEPENDENCIA_ADMINISTRATIVA")
@Data
public class DependenciaAdministrativa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DEPENDENCIA_ADMINISTRATIVA")
	private Long idDependenciaAdministrativa;
	
	@Column(name = "DEPENDENCIA_ADMINISTRATIVA", nullable = false, unique = true)
	private String dependenciaAdministrativa;
	
	@Column(name = "TIPO_DEPENDENCIA", nullable = false, length = 2)
	private String tipoDependencia;
	
	@Column(name = "CNPJ", unique = true, length = 14)
	private String cnpj;
	
	@Column(name = "CEP", unique = true, length = 8)
	private String cep;
	
	@Column(name = "ENDERECO", nullable = false)
	private String endereco;
	
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	@Column(name = "MUNICIPIO", nullable = false)
	private String municipio;
	
	@Column(name = "DISTRITO")
	private String distrito;
	
	@Column(name = "UF", nullable = false, length = 2)
	private String uf;
	
	@Column(name = "DT_CADASTRO", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime dataCadastro;
	
	@Column(name = "ATIVO", nullable = false)
	private Character ativo;
}
