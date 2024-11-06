package br.com.softsy.educacional.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.softsy.educacional.dto.AgendaAnexoDTO;
import br.com.softsy.educacional.dto.CandidatoDTO;
import br.com.softsy.educacional.model.CaminhoImagemRequest;
import br.com.softsy.educacional.service.AgendaAnexoService;

@RestController
@RequestMapping("/agendaAnexo")
public class AgendaAnexoController {

    @Autowired
    private AgendaAnexoService service;

    @GetMapping
    public ResponseEntity<List<AgendaAnexoDTO>> listar() {
        return ResponseEntity.ok(service.listarTudo());
    }

    @GetMapping("/{idAgendaAnexo}")
    public ResponseEntity<AgendaAnexoDTO> buscarPorId(@PathVariable Long idAgendaAnexo) {
        return ResponseEntity.ok(service.buscarPorId(idAgendaAnexo));
    }

    
    @GetMapping("/agenda/{idAgenda}")
    public ResponseEntity<List<AgendaAnexoDTO>> buscarPorIdAgenda(@PathVariable Long idAgenda) {
        List<AgendaAnexoDTO> curso = service.buscarPorIdAgenda(idAgenda);
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<String> getLogoById(@PathVariable("id") Long idAgendaAnexo, @RequestBody CaminhoImagemRequest request) throws IOException {
        String caminho = request.getCaminho();
        String logo = service.getLogoById(idAgendaAnexo, caminho);

        return ResponseEntity.ok(logo);
    }

    @PostMapping
    public ResponseEntity<AgendaAnexoDTO> cadastrar(@RequestBody @Valid AgendaAnexoDTO dto) {
        AgendaAnexoDTO anexoDTO = service.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(anexoDTO.getIdAgendaAnexo()).toUri();
        return ResponseEntity.created(uri).body(anexoDTO);
    }

    @PutMapping
    public ResponseEntity<AgendaAnexoDTO> atualizar(@RequestBody @Valid AgendaAnexoDTO dto) {
        return ResponseEntity.ok(service.atualizar(dto));
    }

    @PutMapping("/{idAgendaAnexo}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long idAgendaAnexo) {
        service.ativaDesativa('S', idAgendaAnexo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idAgendaAnexo}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long idAgendaAnexo) {
        service.ativaDesativa('N', idAgendaAnexo);
        return ResponseEntity.ok().build();
    }
}