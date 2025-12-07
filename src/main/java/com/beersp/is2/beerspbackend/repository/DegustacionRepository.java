package com.beersp.is2.beerspbackend.repository;

import com.beersp.is2.beerspbackend.model.Degustacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegustacionRepository extends JpaRepository<Degustacion, Integer> {

}