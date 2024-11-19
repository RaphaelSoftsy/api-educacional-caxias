package br.com.softsy.educacional.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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

import br.com.softsy.educacional.dto.CadastroAvisoInternoDTO;
import br.com.softsy.educacional.dto.AvisoDTO;
import br.com.softsy.educacional.dto.AvisoInternoDTO;
import br.com.softsy.educacional.service.AvisoInternoService;

@RestController
@RequestMapping("/avisoInterno")
public class AvisoInternoController {

    @Autowired
    private AvisoInternoService avisoInternoService;

  
    @GetMapping
    public ResponseEntity<List<AvisoInternoDTO>> listar() {
        return ResponseEntity.ok(avisoInternoService.listarTudo());
    }

 
    @GetMapping("/{idAvisoInterno}")
    public ResponseEntity<AvisoInternoDTO> buscarPorId(@PathVariable Long idAvisoInterno) {
        return ResponseEntity.ok(avisoInternoService.buscarPorId(idAvisoInterno));
    }

   
    @PostMapping
    public ResponseEntity<CadastroAvisoInternoDTO> cadastrar(@RequestBody @Valid CadastroAvisoInternoDTO dto) throws IOException {
        CadastroAvisoInternoDTO avisoInternoDTO = avisoInternoService.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(avisoInternoDTO.getIdAvisoInterno()).toUri();
        return ResponseEntity.created(uri).body(avisoInternoDTO);
    }

    
    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody @Valid CadastroAvisoInternoDTO dto) {
        return ResponseEntity.ok(avisoInternoService.atualizar(dto));
    }

  
    @PutMapping("/imagem/{id}")
    public ResponseEntity<AvisoInternoDTO> alterarImagemAviso(
            @PathVariable Long id,
            @RequestBody AvisoDTO dto) {
        
        try {
        	AvisoInternoDTO avisoAtualizado = avisoInternoService.alterarImagemAvisoInterno(id, dto.getPathAnexo());
            return ResponseEntity.ok(avisoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    @DeleteMapping("/{idAvisoInterno}")
    public ResponseEntity<Void> excluir(@PathVariable Long idAvisoInterno) {
        avisoInternoService.excluir(idAvisoInterno);
        return ResponseEntity.ok().build();
    }
}
