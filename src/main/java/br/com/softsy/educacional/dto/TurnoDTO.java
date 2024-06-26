package br.com.softsy.educacional.dto;

import java.sql.Time;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import br.com.softsy.educacional.model.Turno;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TurnoDTO {

    private Long idTurno;
    
	@NotNull
	private Long contaId;

    private String mnemonico;

    private String turno;

    private Time horaInicio;

    private Time horaFim;

    private LocalDateTime dataCadastro;
    
    private Character ativo;

    public TurnoDTO(Turno turno) {
        this.idTurno = turno.getIdTurno();
        this.mnemonico = turno.getMnemonico();
        this.turno = turno.getTurno();
        this.horaInicio = turno.getHoraInicio();
        this.horaFim = turno.getHoraFim();
        this.dataCadastro = turno.getDataCadastro();
        this.contaId = turno.getConta().getIdConta();
        this.ativo = turno.getAtivo();
    }
}