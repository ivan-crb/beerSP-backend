package com.beersp.is2.beerspbackend.repository;

import com.beersp.is2.beerspbackend.model.Degustacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegustacionRepository extends JpaRepository<Degustacion, Integer> {

    public List<Degustacion> findByCervezaId(Integer id);

    public List<Degustacion> getDegustacionByUsuarioIdOrderByFechaAlta(Integer id);
}