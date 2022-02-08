package br.com.sergioruivace.beercat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sergioruivace.beercat.model.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long>{

}
