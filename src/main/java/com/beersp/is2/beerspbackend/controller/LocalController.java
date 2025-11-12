package com.beersp.is2.beerspbackend.controller;

import com.beersp.is2.beerspbackend.model.Local;
import com.beersp.is2.beerspbackend.service.LocalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locales")
@AllArgsConstructor
public class LocalController {
    private final LocalService localService;

    @GetMapping("")
    public List<Local> obtenerLocales() {
        return localService.obtenerLocales();
    }

    @GetMapping("/{id}")
    public Local obtenerLocal(@PathVariable int id) {
        return localService.obtenerLocal(id);
    }

    @PostMapping("")
    public Local crearLocal(@RequestBody Local local) {
        return localService.crearLocal(local);
    }

    @PutMapping("/{id}")
    public Local editarLocal(@PathVariable int id, @RequestBody Local local) {
        return localService.actualizarLocal(id, local);
    }

    @DeleteMapping("/{id}")
    public void eliminarLocal(@PathVariable int id) {
        localService.eliminarLocal(id);
    }


}
