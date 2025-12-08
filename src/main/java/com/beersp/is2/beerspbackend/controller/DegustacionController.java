package com.beersp.is2.beerspbackend.controller;

import com.beersp.is2.beerspbackend.model.*;
import com.beersp.is2.beerspbackend.model.Degustacion;
import com.beersp.is2.beerspbackend.service.CervezaService;
import com.beersp.is2.beerspbackend.service.DegustacionService;
import com.beersp.is2.beerspbackend.service.LocalService;
import com.beersp.is2.beerspbackend.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/degustaciones")
@AllArgsConstructor
public class DegustacionController {
    private final DegustacionService degustacionService;
    private final UsuarioService usuarioService;
    private final CervezaService cervezaService;
    private final LocalService localService;

    @GetMapping("")
    public ResponseEntity<List<Degustacion>> obtenerDegustaciones() {
        return ResponseEntity.ok(degustacionService.obtenerDegustaciones());
    }

    @GetMapping("/usuarios/{usuarioId}/ultimos")
    public ResponseEntity<List<Degustacion>> obtenerDegustacionesUltimos(@PathVariable int usuarioId) {
        return ResponseEntity.ok(degustacionService.obtenerDegustacionesUltimos(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Degustacion> obtenerDegustacion(@PathVariable int id) {
        if (!degustacionService.existeDegustacion(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(degustacionService.obtenerDegustacion(id));
    }

    @PostMapping("")
    public ResponseEntity<Void> crearDegustacion(@RequestBody DegustacionAux nuevaDegustacion) {

        Usuario usuario = usuarioService.obtenerUsuario(nuevaDegustacion.getUsuario());
        Cerveza cerveza = cervezaService.obtenerCerveza(nuevaDegustacion.getCerveza());
        Local local = localService.obtenerLocal(nuevaDegustacion.getLocal());

        if (usuario == null || cerveza == null) {
            return ResponseEntity.notFound().build();
        }

        Degustacion degustacion = new Degustacion();
        degustacion.setUsuario(usuario);
        degustacion.setCerveza(cerveza);
        degustacion.setLocal(local);
        degustacion.setCalificacion(nuevaDegustacion.getCalificacion());
        degustacion.setPais(nuevaDegustacion.getPais());
        degustacion.setFechaAlta(nuevaDegustacion.getFechaAlta());

        Degustacion degustacionCreada = degustacionService.crearDegustacion(degustacion);

        degustacionService.actualizarPromedioCerveza(degustacionCreada.getCerveza().getId());

        return ResponseEntity.created(linkTo(DegustacionController.class).slash(degustacionCreada.getId()).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarDegustacion(@PathVariable int id, @RequestBody Degustacion degustacion) {
        if (!degustacionService.existeDegustacion(id)) {
            return ResponseEntity.notFound().build();
        }

        degustacion.setId(id);
        degustacionService.actualizarDegustacion(id, degustacion);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDegustacion(@PathVariable int id) {
        if (!degustacionService.existeDegustacion(id)) {
            return ResponseEntity.notFound().build();
        }

        degustacionService.eliminarDegustacion(id);
        return ResponseEntity.noContent().build();
    }

}