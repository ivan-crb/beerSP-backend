package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Degustacion;
import com.beersp.is2.beerspbackend.repository.DegustacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DegustacionService {
    @Autowired
    private final DegustacionRepository repository;

    public List<Degustacion> obtenerDegustaciones() {
        return repository.findAll();
    }

    public Degustacion obtenerDegustacion(int id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existeDegustacion(int id) { return repository.existsById(id); }

    public Degustacion crearDegustacion(Degustacion degustacion) {
        return repository.save(degustacion);
    }

    public void actualizarDegustacion(int id, Degustacion degustacion) {
        Degustacion degustacionActual =  repository.findById(id).orElse(null);
        if (degustacionActual != null) {
            if (degustacion.getUsuario() != null) degustacionActual.setUsuario(degustacion.getUsuario());
            if (degustacion.getCerveza() != null) degustacionActual.setCerveza(degustacion.getCerveza());
            if (degustacion.getLocal() != null) degustacionActual.setLocal(degustacion.getLocal());
            if (degustacion.getCalificacion() != null) degustacionActual.setCalificacion(degustacion.getCalificacion());
            if (degustacion.getPais() != null) degustacionActual.setPais(degustacion.getPais());
            repository.save(degustacionActual);
        }
    }

    public void eliminarDegustacion(int id) {
        repository.deleteById(id);
    }

    // TODO
    public void actualizarPromedioCerveza(int id) {
        List<Degustacion> a = repository.findByCervezaId(id);



    }
}