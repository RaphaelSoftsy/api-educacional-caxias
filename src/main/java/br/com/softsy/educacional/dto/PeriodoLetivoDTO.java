package br.com.softsy.educacional.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.softsy.educacional.model.PeriodoLetivo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PeriodoLetivoDTO {
    private Long idPeriodoLetivo;
    
    private ContaDTO conta;
    
    private Integer ano;
    
    private Integer periodo;
    
    private LocalDate dtInicio;
    
    private LocalDate dtFim;
    
    private String descricao;
    
    private Character tipoPeriodicidade;
    
    private LocalDateTime dataCadastro;
    
    private Character ativo;
    
    public PeriodoLetivoDTO(PeriodoLetivo periodoLetivo) {
        this.idPeriodoLetivo = periodoLetivo.getIdPeriodoLetivo();
        this.conta = new ContaDTO(periodoLetivo.getConta());
        this.ano = periodoLetivo.getAno();
        this.periodo = periodoLetivo.getPeriodo();
        this.dtInicio = periodoLetivo.getDtInicio();
        this.dtFim = periodoLetivo.getDtFim();
        this.descricao = periodoLetivo.getDescricao();
        this.tipoPeriodicidade = periodoLetivo.getTipoPeriodicidade();
        this.dataCadastro = periodoLetivo.getDataCadastro();
        this.ativo = periodoLetivo.getAtivo();
    }
}