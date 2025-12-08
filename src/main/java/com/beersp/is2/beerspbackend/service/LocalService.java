package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Degustacion;
import com.beersp.is2.beerspbackend.model.Local;
import com.beersp.is2.beerspbackend.repository.DegustacionRepository;
import com.beersp.is2.beerspbackend.repository.LocalRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocalService {
    @Autowired
    private final LocalRepository repository;
    private final DegustacionRepository degustacionRepository;

    public List<Local> obtenerLocales() {
        return repository.findAll();
    }

    public Local obtenerLocal(int id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existeLocal(int id) { return repository.existsById(id); }

    public Local crearLocal(Local local) {
        return repository.save(local);
    }

    public void actualizarLocal(int id, Local local) {
        Local localActual =  repository.findById(id).orElse(null);
        if (localActual != null) {
            if (local.getNombre() != null) localActual.setNombre(local.getNombre());
            if (local.getDireccion() != null) localActual.setDireccion(local.getDireccion());
            repository.save(localActual);
        }
    }

    public void eliminarLocal(int id) {
        repository.deleteById(id);
    }
}