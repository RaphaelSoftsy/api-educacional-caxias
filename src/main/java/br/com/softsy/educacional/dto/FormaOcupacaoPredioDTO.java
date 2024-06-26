package br.com.softsy.educacional.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.softsy.educacional.model.FormaOcupacaoPredio;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormaOcupacaoPredioDTO {

	private Long idFormaOcupacaoPredio;
	
	@NotNull
	private Long contaId;
	
	@NotNull
	private String formaOcupacaoPredio;

	private LocalDateTime dataCadastro;

	private Character ativo;
	
	public FormaOcupacaoPredioDTO(FormaOcupacaoPredio formaOcupacao) {
		this.formaOcupacaoPredio = formaOcupacao.getFormaOcupacaoPredio();
		this.idFormaOcupacaoPredio = formaOcupacao.getIdFormaOcupacaoPredio();
		this.dataCadastro = formaOcupacao.getDataCadastro();
		this.ativo = formaOcupacao.getAtivo();
		this.contaId = formaOcupacao.getConta().getIdConta();
	}
	
}
