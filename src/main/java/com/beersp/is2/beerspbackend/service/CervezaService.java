package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Cerveza;
import com.beersp.is2.beerspbackend.model.Degustacion;
import com.beersp.is2.beerspbackend.repository.CervezaRepository;
import com.beersp.is2.beerspbackend.repository.DegustacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class CervezaService {
    @Autowired
    private final CervezaRepository repository;
    private final DegustacionRepository degustacionRepository;

    public List<Cerveza> obtenerCervezas() {
        return repository.findAll();
    }

    @Transactional
    public List<Cerveza> buscarCervezas(String nombreCerveza) {
        return repository.findByNombreContaining(nombreCerveza);
    }

    @Transactional
    public List<Cerveza> obtenerCervezasUsuario(int usuarioId) {
        List<Degustacion> degustacionesUsuario = degustacionRepository.getDegustacionByUsuarioIdOrderByFechaAlta(usuarioId);
        List<Cerveza> cervezas = new ArrayList<>();
        for (Degustacion degustacion : degustacionesUsuario) {
            if (!cervezas.contains(degustacion.getCerveza())) {
                cervezas.add(degustacion.getCerveza());
            }
        }
        cervezas.sort((o1, o2) -> (int) (100 * (o2.getPromedio() - o1.getPromedio())));
        return cervezas;
    }

    public Cerveza obtenerCerveza(int id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existeCerveza(int id) { return repository.existsById(id); }

    public Cerveza crearCerveza(Cerveza cerveza) {
        return repository.save(cerveza);
    }

    public void actualizarCerveza(int id, Cerveza cerveza) {
        Cerveza cervezaActual = repository.findById(id).orElse(null);
        if (cervezaActual != null) {
            if (cerveza.getNombre() != null) cervezaActual.setNombre(cerveza.getNombre());
            if (cerveza.getFoto() != null) cervezaActual.setFoto(cerveza.getFoto());
            if (cerveza.getDescripcion() != null) cervezaActual.setDescripcion(cerveza.getDescripcion());
            if (cerveza.getEstilo() != null) cervezaActual.setEstilo(cerveza.getEstilo());
            if (cerveza.getProcedencia() != null) cervezaActual.setProcedencia(cerveza.getProcedencia());
            if (cerveza.getTamaño() != null) cervezaActual.setTamaño(cerveza.getTamaño());
            if (cerveza.getFormato() != null) cervezaActual.setFormato(cerveza.getFormato());
            if (cerveza.getPorcentajeAlcohol() != null) cervezaActual.setPorcentajeAlcohol(cerveza.getPorcentajeAlcohol());
            if (cerveza.getAmargor() != null) cervezaActual.setAmargor(cerveza.getAmargor());
            if (cerveza.getColor() != null) cervezaActual.setColor(cerveza.getColor());
            repository.save(cervezaActual);
        }
    }

    public void eliminarCerveza(int id) {
        repository.deleteById(id);
    }
}