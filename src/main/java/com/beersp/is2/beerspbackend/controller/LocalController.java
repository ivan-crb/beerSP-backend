package com.beersp.is2.beerspbackend.controller;

import com.beersp.is2.beerspbackend.model.Local;
import com.beersp.is2.beerspbackend.service.LocalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/locales")
@AllArgsConstructor
public class LocalController {
    private final LocalService localService;

    @GetMapping("")
    public ResponseEntity<List<Local>> obtenerLocales() {
        return ResponseEntity.ok(localService.obtenerLocales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Local> obtenerLocal(@PathVariable int id) {
        if (!localService.existeLocal(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(localService.obtenerLocal(id));
    }

    @PostMapping("")
    public ResponseEntity<Void> crearLocal(@RequestBody Local nuevoLocal) {
        Local local = localService.crearLocal(nuevoLocal);
        return ResponseEntity.created(linkTo(LocalController.class).slash(local.getId()).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarLocal(@PathVariable int id, @RequestBody Local local) {
        if (!localService.existeLocal(id)) {
            return ResponseEntity.notFound().build();
        }

        local.setId(id);
        localService.actualizarLocal(id, local);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocal(@PathVariable int id) {
        if (!localService.existeLocal(id)) {
            return ResponseEntity.notFound().build();
        }

        localService.eliminarLocal(id);
        return ResponseEntity.noContent().build();
    }


}
