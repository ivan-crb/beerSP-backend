package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Cerveza;
import com.beersp.is2.beerspbackend.repository.CervezaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class CervezaService {
    @Autowired
    private final CervezaRepository repository;

    public List<Cerveza> obtenerCervezas() {
        return repository.findAll();
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

    public void actualizarFotoCerveza(int id, MultipartFile foto) {
        Cerveza cervezaActual = repository.findById(id).orElse(null);
        if (cervezaActual != null) {
            cervezaActual.añadirFoto(foto);
            repository.save(cervezaActual);
        }
    }

    public void eliminarCerveza(int id) {
        repository.deleteById(id);
    }


}
