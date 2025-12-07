package com.beersp.is2.beerspbackend.controller;

import com.beersp.is2.beerspbackend.model.Cerveza;
import com.beersp.is2.beerspbackend.service.CervezaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/cervezas")
@AllArgsConstructor
public class CervezaController {
    private final CervezaService cervezaService;

    @GetMapping("")
    public ResponseEntity<List<Cerveza>> obtenerCervezas() {
        return ResponseEntity.ok(cervezaService.obtenerCervezas());
    }

    @GetMapping("/buscar/{nombreCerveza}")
    public ResponseEntity<List<Cerveza>> buscarCervezas(@PathVariable String nombreCerveza) {
        return ResponseEntity.ok(cervezaService.buscarCervezas(nombreCerveza));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cerveza> obtenerCerveza(@PathVariable int id) {
        if (!cervezaService.existeCerveza(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cervezaService.obtenerCerveza(id));
    }

    @PostMapping("")
    public ResponseEntity<Void> crearCerveza(@RequestBody Cerveza nuevaCerveza) {
        Cerveza cerveza = cervezaService.crearCerveza(nuevaCerveza);
        return ResponseEntity.created(linkTo(CervezaController.class).slash(cerveza.getId()).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCerveza(@PathVariable int id, @RequestBody Cerveza cerveza) {
        if (!cervezaService.existeCerveza(id)) {
            return ResponseEntity.notFound().build();
        }
        
        cervezaService.actualizarCerveza(id, cerveza);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCerveza(@PathVariable int id) {
        if (!cervezaService.existeCerveza(id)) {
            return ResponseEntity.notFound().build();
        }

        cervezaService.eliminarCerveza(id);
        return ResponseEntity.noContent().build();
    }


}
