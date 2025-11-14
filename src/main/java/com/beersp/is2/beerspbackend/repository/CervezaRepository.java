package com.beersp.is2.beerspbackend.repository;

import com.beersp.is2.beerspbackend.model.Cerveza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CervezaRepository extends JpaRepository<Cerveza, Integer> {

}