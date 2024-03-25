package br.com.softsy.educacional.dto;

import java.time.LocalDateTime;

import br.com.softsy.educacional.model.NivelEscolaridade;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NivelEscolaridadeDTO {

	private Long idNivelEscolaridade;

	private String nivelEscolaridade;

	private LocalDateTime dataCadastro;

	private Character ativo;

	public NivelEscolaridadeDTO(NivelEscolaridade nivelEscolaridade) {
		this.idNivelEscolaridade = nivelEscolaridade.getIdNivelEscolaridade();
		this.nivelEscolaridade = nivelEscolaridade.getNivelEscolaridade();
		this.dataCadastro = nivelEscolaridade.getDataCadastro();
		this.ativo = nivelEscolaridade.getAtivo();
	}

}
