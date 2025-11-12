package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Local;
import com.beersp.is2.beerspbackend.repository.LocalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocalService {
    private final LocalRepository repository;

    public List<Local> obtenerLocales() {
        return repository.findAll();
    }

    public Local obtenerLocal(int id) {
        return repository.findById(id).orElse(null);
    }

    public Local crearLocal(Local local) {
        return repository.save(local);
    }

    public Local actualizarLocal(int id, Local local) {
        Local localActual =  repository.findById(id).orElse(null);
        if (localActual != null) {
            localActual.setNombre(local.getNombre());
            localActual.setDireccion(local.getDireccion());
            return repository.save(localActual);
        }
        else {
            return null;
        }
    }

    public void eliminarLocal(int id) {
        repository.deleteById(id);
    }


}
