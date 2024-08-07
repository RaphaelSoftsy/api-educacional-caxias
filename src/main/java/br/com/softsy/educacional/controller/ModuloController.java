package br.com.softsy.educacional.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.softsy.educacional.dto.CadastroEscolaDTO;
import br.com.softsy.educacional.dto.CadastroModuloDTO;
import br.com.softsy.educacional.dto.ModuloDTO;
import br.com.softsy.educacional.model.CaminhoImagemRequest;
import br.com.softsy.educacional.service.ModuloService;

@RestController
@RequestMapping("/modulo")
public class ModuloController {

    @Autowired
    private ModuloService service;

    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarTudo() {
        List<ModuloDTO> modulos = service.listarTudo();
        return ResponseEntity.ok(modulos);
    }

    @GetMapping("/{idModulo}")
    public ResponseEntity<ModuloDTO> buscarPorId(@PathVariable Long idModulo) {
        ModuloDTO moduloDTO = service.buscarPorId(idModulo);
        return ResponseEntity.ok(moduloDTO);
    }


    @PostMapping
    public ResponseEntity<CadastroModuloDTO> cadastrar(@RequestBody @Valid CadastroModuloDTO dto) {
    	CadastroModuloDTO moduloDTO = service.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(moduloDTO.getIdModulo()).toUri();
        return ResponseEntity.created(uri).body(moduloDTO);
    }
    
    @GetMapping("/acessos/{idModulo}")
    public ResponseEntity<?> listarAcessosUsuariosModulo(@PathVariable Long idModulo) {
        List<Map<String, Object>> result = service.listarAcessosUsuariosModulo(idModulo);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum acesso encontrado para o usuário ou módulo informado.");
        }
        return ResponseEntity.ok(result);
    }


	@PutMapping
	public ResponseEntity<?> atualizar(@RequestBody @Valid CadastroModuloDTO dto){
		return ResponseEntity.ok(service.atualizar(dto));
	}
	
	@DeleteMapping("/{idModulo}")
	public ResponseEntity<?> excluir(@PathVariable Long idModulo) {
		service.remover(idModulo);
		return ResponseEntity.ok().build();
	}
}
